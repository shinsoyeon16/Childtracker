#include <ESP8266.h>
#include <SoftwareSerial.h>

#define RX_WIFI 10
#define TX_WIFI 11
#define ssid "SY_iPhone2" //wifi SSID 입력
#define PWD "41876852" //wifi password 입력
SoftwareSerial Serial_wifi(RX_WIFI, TX_WIFI);
ESP8266 wifi = ESP8266(Serial_wifi);

char c = "";
String str = "";
String targetStr = "GPGLL";
String latitude;
String longitude;
String device_serial_number = "0000-0000-0000-0024"; //시리얼번호 입력
bool check = false;


void setup() {
  Serial.begin(9600);
  Serial_wifi.begin(9600);
  wifi.begin();
  wifi.setTimeout(3000);
}



void loop() {
  while (check==false){
    if(Serial.available()){
    c = Serial.read(); // 한글자씩 gps모듈에서 읽어옴.
    if (c == '\n') { //한줄이 끝날때까지 str에 저장.
      if (targetStr.equals(str.substring(1, 6))) { //GPGLL 태그가 맞다면 위도경도 파싱.
        Serial.println(str);
        int first = str.indexOf(",");
        int two = str.indexOf(",", first + 1); //latitude
        int three = str.indexOf(",", two + 1); //North
        int four = str.indexOf(",", three + 1); //longitude
        int five = str.indexOf(",", four + 1); //West
        int six = str.indexOf(",", five + 1); //time
        int seven = str.indexOf(",", six + 1); //active or void
        boolean active = (str.substring(six+1,seven).equals("V"))? false : true;
        if(active){
          double data1=str.substring(first+1, two).toFloat();
          double data2=str.substring(three+1, four).toFloat();
          double a = (int)data1/100;
          double b = (int)data2/100;
          double c = (data1-(a*100))/60 + a;
          double d = (data2-(b*100))/60 + b;
          latitude = String(c, 8);
          longitude = String(d, 8);
          check=true;
         }
      }
      str = "";
    } else {
      str += c; //한줄이 끝날때까지 str에 c 한글자씩 저장.
      }
    }
  }
  
  if(check==true){
    if(getStatus(wifi.getAP(ssid)) != "OK"){
      getStatus(wifi.setMode(ESP8266_WIFI_STATION));
      getStatus(wifi.quitAP());
      getStatus(wifi.joinAP(ssid, PWD));
      }
    getStatus(wifi.connect(ESP8266_PROTOCOL_TCP, IPAddress(180, 229, 37, 231), 8080));
    String cmd = "GET /childtrackerserver/writedata.php?device_serial_number=";
    cmd += device_serial_number;
    cmd += "&latitude=";
    cmd += latitude;
    cmd += "&longitude=";
    cmd += longitude;
    cmd += " HTTP/1.0\r\n\r\n";
    getConnectionStatue(wifi);
    if (wifi.send(cmd)) {
      latitude="";
      longitude="";
    }
    check = false;
  }
    delay(60000);
}

String getStatus(bool status)
{
  if (status)
    return "OK";

  return "KO";
}

String getStatus(ESP8266CommandStatus status)
{
  switch (status) {
    case ESP8266_COMMAND_INVALID:
      return "INVALID";
      break;

    case ESP8266_COMMAND_TIMEOUT:
      return "TIMEOUT";
      break;

    case ESP8266_COMMAND_OK:
      return "OK";
      break;

    case ESP8266_COMMAND_NO_CHANGE:
      return "NO CHANGE";
      break;

    case ESP8266_COMMAND_ERROR:
      return "ERROR";
      break;

    case ESP8266_COMMAND_NO_LINK:
      return "NO LINK";
      break;

    case ESP8266_COMMAND_TOO_LONG:
      return "TOO LONG";
      break;

    case ESP8266_COMMAND_FAIL:
      return "FAIL";
      break;

    default:
      return "UNKNOWN COMMAND STATUS";
      break;
  }
}
void getConnectionStatue(ESP8266 wifi) {
  // getConnectionStatus
  ESP8266ConnectionStatus connectionStatus;
  ESP8266Connection connections[5];
  unsigned int connectionCount;
  Serial.print("getConnectionStatus: ");
  Serial.print(getStatus(wifi.getConnectionStatus(connectionStatus, connections, connectionCount)));
  Serial.print(" : ");
  Serial.println(connectionCount);
  for (int i = 0; i < connectionCount; i++) {
    Serial.print(" - Connection: ");
    Serial.print(connections[i].id);
    Serial.print(" - ");
    Serial.print(getProtocol(connections[i].protocol));
    Serial.print(" - ");
    Serial.print(connections[i].ip);
    Serial.print(":");
    Serial.print(connections[i].port);
    Serial.print(" - ");
    Serial.println(getRole(connections[i].role));
  }
}
String getRole(ESP8266Role role)
{
  switch (role) {
    case ESP8266_ROLE_CLIENT:
      return "CLIENT";
      break;

    case ESP8266_ROLE_SERVER:
      return "SERVER";
      break;

    default:
      return "UNKNOWN ROLE";
      break;
  }
}
String getProtocol(ESP8266Protocol protocol)
{
  switch (protocol) {
    case ESP8266_PROTOCOL_TCP:
      return "TCP";
      break;

    case ESP8266_PROTOCOL_UDP:
      return "UDP";
      break;

    default:
      return "UNKNOWN PROTOCOL";
      break;
  }
}
