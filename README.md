# IBM Workshop Guide for **MQ on CP4I**

## Overview  

<!--- cSpell:ignore gitorg YAMLs -->

The guide is to help users to set up IBM MQ on Cloud after being installed. Only simple use case is included.

To be more specific, the following items are included:

-   Queue Manager
-   ConfigMap
-   Secret
-   Channel
-   Route

```
openssl req -newkey rsa:2048 -nodes -keyout tls.key -subj "/CN=localhost" -x509 -days 3650 -out tls.crt
```
Two files (tls.crt and tls.key) are created by openssl.

Execute the following commands and replace the strings (line 36 and 37) inside the demo-mq.yaml file with the output. 
```
base64 tls.crt 
base64 tls.key 
```

Apply yaml file to OpenShift to create a QueueManager
```
oc apply -f demo-mq.yaml
```

Execute this script to create key store. This keystore file will be used by client applications to establish TLS secured connection to MQ.
```
keytool -import -trustcacerts -alias democrt -file tls.crt -keystore /your/local/file/system/demo.jks
```
