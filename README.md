# HikingPlanner Backend Repository

## 📌등산로 이상 신고
controller > trailReport
-프론트 보고 수정하겠음
```
//Request body 예시
{
  "report": "바위 조심.",
  "latitude": "35.6585",
  "longitude": "139.7454",
  "timestamp": "2024-10-20T12:00:00Z"
}
```

## 📌긴급 신고
controller > SosController
- sos 버튼 클릭 시, **/sos API 호출(POST)** 
```
// Request body 예시
{
  "userid": "1",
  "latitude": 37.321908,
  "longitude": 127.124,
  "date": "2024-05-16T00:32:00Z"
}
```

-> 국가지점번호와 사용자 정보 반환
```
//Response Body
{
  "username": "김하플",
  "phone_number": "010-1234-5678",
  "latitude": 37.321908,
  "longitude": 127.124,
  "nationalposnum": "다사 6668 2483",
  "time": "2024-05-16T00:32:00"
}
```
- sos 버튼 클릭 후, 3초가 지나면 **/sendsosmessage API 호출(POST)**
```
// Request body 예시
{
  "username": "김하플",
  "phone_number": "010-1234-1234",
  "nationalposnum": "다사 6668 2483",
  "time": "2024-06-22T10:19:07.414Z"
}
```
-> application.properties에 지정된 번호로 신고문자 전송
  
#### ※위도 경도값을 국가지점번호로 반환하는 알고리즘
1. GRS80 -> UTM-K // 좌표계 변환 proj4j 라이브러리 사용
2. UTM-K 좌표로 국가지점번호 계산 // calNationalPointNumber()
```
// service > GetNationalPointNumberService.java
public class GetNationalPointNumberService {

  public String getNationalPointNumber(double latitude, double longitude){
    double[] resultUTMK = convertGRS80toUTMK(latitude, longitude);
    String result = calNationalPointNumber(resultUTMK[0], resultUTMK[1]);
    return result;
  }
  public double[] convertGRS80toUTMK(double latitude, double longitude) {
    CRSFactory crsFactory = new CRSFactory();
    CoordinateReferenceSystem grs80System = crsFactory.createFromName("EPSG:4326");
    CoordinateReferenceSystem utmkSystem = crsFactory.createFromName("EPSG:5179");

    ProjCoordinate grs80Coord = new ProjCoordinate(longitude, latitude);
    ProjCoordinate utmCoord = new ProjCoordinate();

    CoordinateTransform transform = new BasicCoordinateTransform(grs80System, utmkSystem);
    transform.transform(grs80Coord, utmCoord);   
    return new double[]{utmCoord.x, utmCoord.y};
  }

  public String calNationalPointNumber(double x, double y) {
    try {
        // 동향값 E, East x
        String[][] HAN_E = {
                {"7", "가"}, {"8", "나"}, {"9", "다"}, {"10", "라"},
                {"11", "마"}, {"12", "바"}, {"13", "사"}, {"14", "아"}
        };

        // 북향값 N, North y
        String[][] HAN_N = {
                {"13", "가"}, {"14", "나"}, {"15", "다"}, {"16", "라"},
                {"17", "마"}, {"18", "바"}, {"19", "사"}, {"20", "아"}
        };

        String x2 = String.format("%d", (int)Math.ceil(x));
        String y2 = String.format("%d", (int)Math.ceil(y));

        int restCount = 4;
        
        // 앞 두자리를 숫자를 잘라서 해당 숫자의 한글을 가져오고, 나머지 좌표를 잘라서 합친다.
        String nationalPointNumber =
            getHanLetter(HAN_E,x2.substring(0, x2.length() == 6 ? 1 : 2)) +
          getHanLetter(HAN_N,y2.substring(0, 2)) +
                " " +
                x2.substring((x2.length() == 6 ? 1 : 2), (x2.length() == 6 ? 3 : 4) + 2) +
                " " +
                y2.substring(2, restCount + 2);
        
        return nationalPointNumber;
  } catch (Exception e) {
    log.info(e.getMessage());
    return "";
    }
  }
  public String getHanLetter(String[][] array, String number) {
      for (int i=0; i < array.length; i++) {

        
          if (array[i][0].equals(number)) {
              return array[i][1];
          }
      }
      return "";
  }
}
```
## 📌많이 간 등산로 제공
1. 등산시작 > 등산종료 시, 사용자들의 등산기록 DB에 저장 -> **/hiking_record API 호출(POST)**
    ```
    //Request Body
    {
      "recordid": 0,
      "userid": "1",
      "mtid": 1,
      "hikingTrailData": [
        {
          "latitude": 37.7749,
          "longitude": -122.4194,
          "timestamp": 1657890123000
        },
        {
          "latitude": 37.775,
          "longitude": -122.4195,
          "timestamp": 1657890183000
        },
        {
          "latitude": 37.7753,
          "longitude": -122.4197,
          "timestamp": 1657890184000
        },
        {
          "latitude": 37.7751,
          "longitude": -122.4196,
          "timestamp": 1657890243000
        }
      ],
      "savetime": 1657890243000
    }
    ```

2. **/updatetrail/{mountainid} API 호출(GET)** 시, 각 산 별 '유저들이 많이 간 경로' 업데이트

   파이썬 서버로 DB의 각 산 별 유저들의 등산기록을 전송하고, 파이썬서버에서 도출한 경로정보를 DB에 저장한다.
   만약 기존에 '유저들이 많이 간 경로'가 DB에 없다면 새로 생성하고, 있다면 경로 정보를 수정한다.
   
   <a href="https://github.com/Hiking-Planner/PythonClusteringSever"> 경로 클러스터링 서버 readme 바로가기 </a>
