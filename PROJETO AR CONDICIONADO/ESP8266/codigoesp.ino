thi#include <DHT11.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>


const char* ssid = "iPhone";
const char* password = "12345678";
const char* mqtt_server = "172.20.10.9";
char topic[] = "topic";

WiFiClient wifiClient;
PubSubClient client(wifiClient);
int pin = 2;
DHT11 dht11(pin);
int err; 
float temp, humi;

void setup()
{

  Serial.begin(115200);
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.print(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");

  Serial.print("WiFi connected, IP address: ");
  Serial.println(WiFi.localIP());

  client.setServer(mqtt_server, 1883);
  // client.setCallback(callback);
}

void loop()
{
  /*
    if (!client.connected()) {
    reconnect();
    }
    if(!client.loop())
    client.connect("ESP8266Client");

    now = millis();
    if (now - lastMeasure > 15000) {
    lastMeasure = now;
    float t = dht11.read();

    if (isnan(t)) {
      Serial.println("Falha em ler sensor DHT!");
      return;
    }

    static char temperatureTemp[7];
    dtostrf(t, 6, 2, temperatureTemp);

    client.publish("MQTT/temperatura", temperatureTemp);

    Serial.print("Temperatura: ");
    Serial.print(t);
    Serial.print(" ºC ");
    }
  */
  if (!!!client.connected()) {
    Serial.print("Reconnecting client to ");
    Serial.println(mqtt_server);
    while (!!!client.connect("ESP8266Client")) {
      Serial.print(".");
      delay(500);
    }
    Serial.println();
  }

  if ((err = dht11.read(humi, temp)) == 0)
  {
    Serial.print("temperature:");
    Serial.print(temp);
    Serial.print(" humidity:");
    Serial.print(humi);
    Serial.println();
    String payload = "{";
    payload += "\"temperature\":";
    payload += temp;
    payload += ",\"humidity\":";
    payload += humi;
    payload += "}";
    Serial.print("Sending payload: ");
    Serial.println(payload);

    if (client.publish(topic, (char*) payload.c_str())) {
      Serial.println("Publish ok");
    } else {
      Serial.println("Publish failed");
    }
    delay(10000);
  }
}

