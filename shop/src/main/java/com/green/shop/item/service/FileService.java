package com.green.shop.item.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class FileService {


    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData)
                                    throws Exception {     //파일은 예외처리 필수
        //고유성을 보장해주는 값
        //시간, 컴퓨터의 MAC주소, 무작위값 활용하여서 아주 낮은 확률로 중복되는 값을 생성하는데 사용
        UUID uuid = UUID.randomUUID();

        //마지막 점(.) 이후의 문자열을 추출 -> 확장자를 가지고온다.
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        //UUID의 값과 확장자를 붙여서 새로운 파일이름을 생성
        String savedFileName = uuid.toString() + extension;

        //파일이 업로드될 전체 경로를 생성
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        //입출력 스트림을 생성하고 파일 경로에 해당하는 파일을 저장
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        //파일을 작성
        fos.write(fileData);

        //파일의 출력스트림을 종료
        fos.close();

        //저장된 파일 이름을 리턴
        return savedFileName;

    }

    //상품 이미지를 변경하기전에 기존 이미지 삭제
    public void deleteFile(String filePath) throws Exception{
        //파일 객체 생성
        File deleteFile = new File(filePath);

        //파일이 존재하는지 확인
        if(deleteFile.exists()){
            //삭제
            deleteFile.delete();
            System.out.println("파일을 삭제하였습니다.");
        } else{
            System.out.println("파일이 존재하지 않습니다.");
        }
    }



}
