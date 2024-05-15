package com.hikingplanner.hikingplanner.service;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
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