#!/usr/bin/php
<?php
error_reporting(E_ALL);
ini_set("display_errors", 1);
$JIRA_USER="tester";
$JIRA_PASSWORD="tdd321";
 
// Test쪽에서 넘겨받을 정보 - 센터버전, 지라이슈번호, TC개수, 테스트 결과, 수행시간
$i = 0;
$CENTER_IMG = $argv[++$i];
$REPORT_ISSUE = $argv[++$i];
$RESULT = $argv[++$i];
$ASSIGNER = $argv[++$i];

$VERSION_FULL = preg_replace('/(.*-[0-9]{5,6}-|\.[0-9]{4}.*)/', "", $CENTER_IMG);
if (strlen($VERSION_FULL) == 7) {
	$SUBSTR_LEN = 5;
} else {
	$SUBSTR_LEN = 3;
}
$SENSOR_IMG = str_replace("CT64", "SS64", $CENTER_IMG);
$VERSION = substr($VERSION_FULL, 0, $SUBSTR_LEN);
$REVISION = preg_replace('/(.*-(C|B|R)-|-[1-9]\\.[0-9].*)/', "", $CENTER_IMG);
$PACKAGE = preg_replace('/(NAC-|-(C|B|R)-.*)/', "", $CENTER_IMG);

/* 상황별 RESULT 변수 값
자동테스트 시작 시 : TEST_START
오류(테스트환경) 시 : BASIC_TEST_ERROR
테스트 실패시(기능오류) : FUNCTION_TEST_ERROR
테스트 성공시 (성공율 만족) : TEST_SUCCESS
테스트 성공시 (성공율 불만족) : TEST_FAIL
*/
// JIRA 코멘트 상황 별 제목 , 이슈 상태 ID 변경 필요
if ($RESULT == "TEST_START") {
    $SUBJECT = "[자동테스트-시작함(2)] \n" .
    "개발자 자동테스트가 시작되었습니다.";
} else if ($RESULT == "BASIC_TEST_ERROR") {
    $SUBJECT = "[자동테스트-테스트환경 오류(E2)] \n" .
    "{color:red}QA에서 오류 해결후, 자동테스트 재시작 예정{color}";
    // QA FAILED 이슈 상태 ID 값 확인하여 수정 (현재 : OPEN ID 값)
    $transition["id"] = "1071";
} else if ($RESULT == "FUNCTION_TEST_ERROR") {
    $SUBJECT = "[자동테스트 결과-기능오류 발견(E3)] \n" .
    "{color:red}소스 수정 후 JIRA 상태를 'Test Request'로 변경해 주세요. \n
    단, 관련없는 오류인 경우는 'QA Review'로 변경해 주세요.{color}";
    // Test Failed 이슈 상태 ID 값 확인하여 수정 (현재 : OPEN ID 값)
    $transition["id"] = "1311";
} else if($RESULT == "TEST_SUCCESS") {
    $SUBJECT = "[자동테스트 결과 - 성공(3)] \n" .
    "명백한 오류가 없습니다. 'QA Review'로 자동 전환됩니다.";
    // Test Succesed 이슈 상태 ID 값 확인하여 수정 (현재 : RESOLVED ID 값)
    $transition["id"] = "1311";
} else if ($RESULT == "TEST_FAIL") {
    $SUBJECT = "[자동테스트 결과 - 실패(소스 수정 필요)(3)] \n" .
    "{color:red}성공율 불만족으로 'Test Failed'로 자동 전환됩니다. \n
    소스 수정후 자동테스트 재요청해 주세요.{color}";
    // 성공률 실패 시 이전상태로 갈 것 인지 TEST FAILED로 갈것인지 정해야함
    // Test Failed 이슈 상태 ID 값 확인하여 수정 (현재 : OPEN ID 값)
    $transition["id"] = "1311";
}

$ji = new JIRA_REST($JIRA_USER, $JIRA_PASSWORD);

// JIRA 상태 변경, 성공률 체크
$issue_status["transition"] = $transition;
if($RESULT != "TEST_START") {
    $C_STAT = $ji->editIssueStatus($REPORT_ISSUE,$issue_status);
    
    // 구글드라이브(기술연구소-테스트) 리포트
    $REPORT_NAME = str_replace("-", "", $REPORT_ISSUE);
    $readHTML = "C:/CLOUD/test/v6/report/$REVISION/$REPORT_NAME" . "_" . "$REVISION". "_extent.html"; // 리포트 파일 경로
    /* 성공률 정보, HTML 형식이 변경되지 않음으로 고정 (getFailEventsValue 함수에서 액션 성공, 실패를 저장하는 변수 자체를 참조)
     만약 extent report 버전 업그레이드 또는 다른 문제로 인해 성공률이 제대로 찍히지 않을 경우 getFailEventsValue Function에서
     참조하는 변수명 확인 필요 */
    $failEventsValue = getFailEventsValue($readHTML);
}

// JIRA 상황 별 코멘트
$body = "$SUBJECT \n" ;

//TEST_START는 추가 내용이 없음
if ($RESULT == "TEST_START") {
    $body = $body . "\n {quote} *테스트 버전 :* $CENTER_IMG \n" ;   
} else if ($RESULT == "BASIC_TEST_ERROR") { //환경설정 오류
    $body = $body . "\n {quote} *테스트 버전 :* $CENTER_IMG \n" .
    "*리비전 번호 :* $REVISION \n" ;
} else if ($RESULT == "FUNCTION_TEST_ERROR" || $RESULT == "TEST_SUCCESS" || $RESULT == "TEST_FAIL") {
    $body = $body . "\n {quote} *테스트 버전 :* $CENTER_IMG \n" .
    "*리비전 번호 :* $REVISION \n" .
    "*성공률 :* $failEventsValue % \n" .
    "*레포트 :* https://drive.google.com/drive/folders/1RZvCnF2Shy8iPvXtaf5kkganmLct_Aek \n" ; // 수정 필요, 
} 

$body = $body . "{quote}";
// JIRA 코멘트 작성
$comment["body"] = $body;

$A_RET = $ji->comment_add($REPORT_ISSUE,$comment);
 
/* Slack Message */

if ($RESULT == "BASIC_TEST_ERROR"){
    $message = "[자동테스트-테스트 환경 오류(E2)] \n" .
    "*오류 해결이 필요합니다.* \n" .
    "- JIRA # : $REPORT_ISSUE \n" ;
    postMessage("#rnd-planning-qa", $message);
} else if ($RESULT == "FUNCTION_TEST_ERROR"){
    $message = "[자동테스트 결과-기능오류 발견(E3)] \n" .
    "*기능오류를 확인후 조치해 주세요.* \n" .
    "- JIRA # : $REPORT_ISSUE \n" ;
    postMessage("@$ASSIGNER", $message);
} else if ($RESULT == "TEST_FAIL"){
    $message = "[자동테스트 결과 - 실패(소스 수정 필요)(3)] \n" .
    "*성공율 불만족으로 '이전 상태'로 자동 전환됩니다. \n" .
    "소스 수정후 자동테스트 재요청해 주세요.* \n" . 
    "- JIRA # : $REPORT_ISSUE \n" ;
    postMessage("@$ASSIGNER", $message);
}

function postMessage($channel, $message)
{
	$data = "payload=" . json_encode(array(
			"channel"       =>  $channel,
			"text"          =>  $message
	));

	$ch = curl_init("https://hooks.slack.com/services/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");
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
	curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	$result = curl_exec($ch);
	curl_close($ch);

	if ($result != "ok") {
		error_exit("postMessage: " . $result);
	}
}
function slackGetUserChannels($userId) {
    $token = "xapp-1-A05SNCE4WLV-5906440547077-48d12b8872d94f16b18bc67e679168c25045f6c5127f24aadde0f5771726b4e4";
    // Slack API 엔드포인트 URL
    $apiUrl = "https://slack.com/api/conversations.list";

    // API 호출을 위한 HTTP 헤더
    $headers = array(
        "Authorization: Bearer $token",
    );

    // API 호출 시 사용할 파라미터 설정
    $params = array(
        "types" => "public_channel,private_channel",
    );

    // 파라미터를 URL 형식으로 인코딩
    $query = http_build_query($params);

    // cURL 세션 초기화
    $ch = curl_init("$apiUrl?$query");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

    // API 호출 및 응답 받기
    $response = curl_exec($ch);

    // cURL 세션 닫기
    curl_close($ch);

    // 응답 데이터를 JSON으로 디코딩
    $data = json_decode($response, true);

    // 사용자의 채널 목록을 저장할 배열 초기화
    $userChannels = array();

    // API 응답에서 사용자가 속한 채널 추출
    if (isset($data['ok']) && $data['ok'] === true && isset($data['members'])) {
        foreach ($data['members'] as $member) {
            if (isset($member['name']) && $member['name'] === $username) {
                return $member['id'];
            }
        }
    }

    return null;
}

function error_exit($msg)
{
	fprintf(STDERR, "\n");
	fprintf(STDERR, "-------------------------------------------------------------------------------\n");
	fprintf(STDERR, "ERROR: %s\n", $msg);
	fprintf(STDERR, "-------------------------------------------------------------------------------\n");
	exit(255);
}

// Test 성공률 계산
function getFailEventsValue($url) {
    $html = file_get_contents($url);

    // 정규 표현식을 사용하여 "passEvents: pass 액션 수" 값을 추출
    if(preg_match('/passEvents:\s*(\d+)/', $html, $pass_matches)){
    $passEventsValue = $pass_matches[1];
    $passEventsValue = trim(strtolower(str_replace('passEvents:', '', $passEventsValue)));
    } else {
        return "Pass Value not found";
    }

    // 정규 표현식을 사용하여 "failEvents: fail 액션 수" 값을 추출
    if (preg_match('/failEvents:\s*(\d+)/', $html, $fail_matches)) {
        $failEventsValue = $fail_matches[1];
        $failEventsValue = trim(strtolower(str_replace('failEvents:', '', $failEventsValue)));
    } else {
        return "Fail Value not found";
    }
   
    $rate = number_format((($passEventsValue / ($passEventsValue + $failEventsValue)))*100,2);
    return $rate;
}



class RestClient {
    
    public $options;
    public $handle; // cURL resource handle.
    
    // Populated after execution:
    public $response; // Response body.
    public $headers; // Parsed reponse header object.
    public $info; // Response info object.
    public $error; // Response error string.
    
    public function __construct($options=array()){
        $this->options = array_merge(array(
            'headers' => array(), 
            'curl_options' => array(), 
            'user_agent' => "PHP RestClient/0.1", 
            'base_url' => NULL, 
            'format' => NULL, 
            'username' => NULL, 
            'password' => NULL
        ), $options);
    }
    
    public function set_option($key, $value){
        $this->options[$key] = $value;
    }
    
    public function get($url, $parameters=array(), $headers=array()){
        return $this->execute($url, 'GET', $parameters, $headers);
    }
    
    public function post($url, $parameters=array(), $headers=array()){
        return $this->execute($url, 'POST', $parameters, $headers);
    }
    
    public function put($url, $parameters=array(), $headers=array()){
        return $this->execute($url, 'PUT', $parameters, $headers);
    }
    
    public function delete($url, $parameters=array(), $headers=array()){
        return $this->execute($url, 'DELETE', $parameters, $headers);
    }
    
    public function format_query($parameters, $primary='=', $secondary='&'){
        $query = "";
        foreach($parameters as $key => $value){
            $pair = array(urlencode($key), urlencode($value));
            $query .= implode($primary, $pair) . $secondary;
        }
        return rtrim($query, $secondary);
    }
    
    public function parse_response($response){
        $headers = array();
        $http_ver = strtok($response, "\n");
        
        while($line = strtok("\n")){
            if(strlen(trim($line)) == 0) break;
            
            list($key, $value) = explode(':', $line, 2);
            $key = trim(strtolower(str_replace('-', '_', $key)));
            $value = trim($value);
            if(empty($headers[$key])){
                $headers[$key] = $value;
            }
            elseif(is_array($headers[$key])){
                $headers[$key][] = $value;
            }
            else {
                $headers[$key] = array($headers[$key], $value);
            }
        }
        
        $this->headers = (object) $headers;
        $this->response = strtok("");
    }
    
    public function execute($url, $method='GET', $parameters=array(), $headers=array()){
        $client = clone $this;
        $client->url = $url;
        $client->handle = curl_init();
        $curlopt = array(
            CURLOPT_HEADER => TRUE,
            CURLOPT_RETURNTRANSFER => TRUE,
            CURLOPT_USERAGENT => $client->options['user_agent']
        );
        
        if($client->options['username'] && $client->options['password'])
            $curlopt[CURLOPT_USERPWD] = sprintf("%s:%s", 
                $client->options['username'], $client->options['password']);
        
        if(count($client->options['headers']) || count($headers)){
            $curlopt[CURLOPT_HTTPHEADER] = array();
            $headers = array_merge($client->options['headers'], $headers);
            foreach($headers as $key => $value){
                $curlopt[CURLOPT_HTTPHEADER][] = sprintf("%s:%s", $key, $value);
            }
        }
        
        if($client->options['format'])
            $client->url .= '.'.$client->options['format'];
       
	   	switch (strtoupper($method)) {
		case 'POST':
            $curlopt[CURLOPT_POST] = TRUE;
            //$curlopt[CURLOPT_POSTFIELDS] = $client->format_query($parameters);
            $curlopt[CURLOPT_POSTFIELDS] = $parameters;
		break;
		case 'PUT':
            $curlopt[CURLOPT_CUSTOMREQUEST] =  "PUT";
            $curlopt[CURLOPT_POSTFIELDS] = $parameters;
		break;
		case 'DELETE':
            $curlopt[CURLOPT_CUSTOMREQUEST] =  "DELETE";
            $curlopt[CURLOPT_POSTFIELDS] = $parameters;
		break;

		default:
        if(count($parameters)){
            $client->url .= strpos($client->url, '?')? '&' : '?';
            $client->url .= $client->format_query($parameters);
        }
		break;
		}
        
        if($client->options['base_url']){
            if($client->url[0] != '/' || substr($client->options['base_url'], -1) != '/')
                $client->url = '/' . $client->url;
            $client->url = $client->options['base_url'] . $client->url;
        }
        $curlopt[CURLOPT_URL] = $client->url;
        
        if($client->options['curl_options']){
            // array_merge would reset our numeric keys.
            foreach($client->options['curl_options'] as $key => $value){
                $curlopt[$key] = $value;
            }
        }
        curl_setopt_array($client->handle, $curlopt);
        
        $client->parse_response(curl_exec($client->handle));
        $client->info = (object) curl_getinfo($client->handle);
        $client->error = curl_error($client->handle);
        
        curl_close($client->handle);
        return $client;
    }
}
class JIRA_REST {
	var $rc;
	var $userid;
	var $password;
	var $base_url = "https://ims.genians.com/jira/rest/api/2";

	public function __construct($userid,$password){
		$this->userid = $userid;
		$this->password = $password;
		$this->rc = new RestClient(array(
    		'curl_options' => array(CURLOPT_SSL_VERIFYHOST => 0,CURLOPT_SSL_VERIFYPEER => false
			,CURLOPT_HTTPHEADER => array('Content-Type: application/json','Accept: application/json')
			)
        ));
	}
	public function issue_crmlink($from, $to) {
		$url = $this->base_url . "/issueLink";
		$this->set_auth_user();

		$A_IN['type']['name'] = "2.고객관리";
		$A_IN['inwardIssue']['key'] = $from;
		$A_IN['outwardIssue']['key'] = $to;

		print(json_encode($A_IN));

		$r = $this->rc->post($url, json_encode($A_IN));

		print_r($r);
	}

	public function common_get($url) {
		if (! $url) return false;

		$this->set_auth_user();
		$r = $this->rc->get($url);
		$R = $this->return_value($r,$url);
		return $R;
	}

	private function set_auth_user() {
		$this->rc->set_option('username',$this->userid);
		$this->rc->set_option('password',$this->password);
	}

    private function return_value($r,$url = '') {
	    var_dump($r->response);
        $res = json_decode($r->response,TRUE);
        var_dump($res);
        if (! $res) $res = true;
        if ((substr($r->info->http_code,0,1)) == '2') {
            return $res;
        } else {
            //$_SESSION['ERRMSG'][] = $r->response;
			//error_write($r->response);
            return false;
        }
    }

    public function comment_add($issuekey,$A_IN) {
        if (! $issuekey) return false;
        if (! $A_IN['body']) return false;
        $this->set_auth_user();
        $url = $this->base_url . "/issue/$issuekey/comment";
        $r = $this->rc->post($url, json_encode($A_IN));
        var_dump($r);
        $R = $this->return_value($r,$url);
        return $R;
    }
    // 이슈 상태 변경
    public function editIssueStatus($issueKey,$A_IN) {
        if (!$issueKey) return false;
        if (! $A_IN['transition']) return false;
        $this->set_auth_user();
        $url = $this->base_url . "/issue/$issueKey/transitions";
        $r = $this->rc->post($url, json_encode($A_IN));
        var_dump($r);
        $R = $this->return_value($r,$url);
        return $R;
    }
}
?>