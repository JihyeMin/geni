package com.genians;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.genians.common.DriveQuickstart;

public class TestRun {
    public static void main(String[] arg) {
    
        String testImg = arg[0]; //테스트 이미지:
        String issue = arg[1]; //테스트 이슈
        String assigner = arg[2]; //ASSIGNER
        String azAgent = arg[3]; //Azure Agent 이름
        String azDkns = arg[4]; //Azure DKNS 이름
        String revision = testImg.split("-")[3];


        //String testImg = "NAC-CLOUD-C-119925-6.0.18.0921"; //테스트 이미지:
        issue = "GN-27165"; //테스트 이슈
        //String azAgent = "Agent00"; //Azure Agent 이름
        //String azDkns = "dkns00"; //Azure DKNS 이름
        //String revision = testImg.split("-")[3];
        String result = "TEST_START";
        String filePath = "c:/test_result.txt";

        long startTime;
        long endTime;
        long elapsedTime;
        String runtime;

        issue = "GN-27165"; // 테스트를 위한 이슈 번호 고정

        writeFile("TEST_START"); // 테스트 시작시 c:/test_result.txt 에 기록
        startTime = System.currentTimeMillis();
    /*
        자동테스트 시작 시 : TEST_START
        오류(테스트환경) 시 : BASIC_TEST_ERROR
        테스트 실패시(기능오류) : FUNCTION_TEST_ERROR
        테스트 성공시 (성공율 만족) : TEST_SUCCESS
        테스트 성공시 (성공율 불만족) : TEST_FAIL
    */


        //JIRA /Comments 기록
        //테스트 시작 코멘트 (php .\jira.report.php NAC-CT64-R-119560-5.0.55.0912 GN-27165 TEST_START)
        execWinCommand("cmd /c cd C:/CLOUD/test/v6 && php ./jira.report.php " + testImg + " " + issue + " " + result);

        // 테스트 시작
        // 기능 단위 테스트를 위한 코드 추가 필요.
        execWinCommand("cmd /c cd C:/CLOUD/test/v6 && mvn test -DtargetVersion=" + testImg + " -DtargetJiraNum=" + issue);


        result = readFile(filePath);

        /*
        if (result != "TEST_START") { //기능테스트에 대한 값을 result 에 기록해야 함 수정 필요, (TEST_START = 정상)
            writeFile(result); // result에결과를 파일에 기록
            result = readFile(filePath);
        }
         */

        //오류(테스트환경) 시 리포팅 후 종료, 성공시 테스트 계속 진행
        if (result == "BASIC_TEST_ERROR") { 
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            runtime = formatElapsedTime(elapsedTime);

            execWinCommand("cmd /c cd C:/CLOUD/test/v6 && php ./jira.report.php " + testImg + " " + issue + " " + result + " " + runtime);
        } else {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            runtime = formatElapsedTime(elapsedTime);
            // execWinCommand("cmd /c cd C:/CLOUD/test/v6 && mvn test -DtargetVersion=" + testImg + " -DtargetJiraNum=" + issue); 기능별 테스트 

            // 기능별 테스트 결과에 따른 리포팅
            if (result == "FUNCTION_TEST_ERROR") {

            } else if (result == "TEST_FAIL") {

            } else if (result == "TEST_SUCCESS") {

            }

            execWinCommand("cmd /c cd C:/CLOUD/test/v6 && php ./jira.report.php " + testImg + " " + issue + " " + result + " " + runtime);
        }
        //체크 코드 처리 필요
        
        // 테스트 리포트 드라이브 전송     // 공유드라이브로 변경 필요    
        try {
            String folderId = "1Zww4BJ0f1MQhN4dPyGY_IJV4W1EyPEo0"; // Google 드라이브 폴더 ID
            String localFolderPath = "C:/CLOUD/test/v6/report/" + revision; // 로컬 폴더 경로
            System.out.println(localFolderPath);

            DriveQuickstart.uploadFolderToDrive(folderId, localFolderPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        

        // 파이프라인 호출 
        execWinCommand("cmd /c curl -X POST -u :zg577vpdgfgaptdltyvmiacjf4ooefoswda7q3lumx54neesgcpa \"https://dev.azure.com/Automated-Test/Automated-Test/_apis/pipelines/9/runs?api-version=7.1-preview.1\" -H \"Content-Type: application/json\" -d \"{\\\"stagesToSkip\\\": [], \\\"variables\\\": {\\\"Agent\\\": {\\\"value\\\": \\\"" + azAgent + "\\\"}, \\\"dkns\\\": {\\\"value\\\": \\\"" + azDkns + "\\\"}}}\"");

    }

    public static void execWinCommand(String cmd) {
		try {
			final Process pr = new ProcessBuilder("cmd", "/C", cmd).redirectErrorStream(true).start();
			    try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					String line = "";
					while ((line = br.readLine()) != null) {
						System.out.println(line);
					}
				} catch (IOException e) {
					System.out.println(e);
				}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

    public static void writeFile(String testresult) {
        String filePath = "c:/test_result.txt";

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(testresult);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static String formatElapsedTime(long elapsedTime) {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;

        return hours + "hour" + minutes + "minute" + seconds + "second";
    }
}