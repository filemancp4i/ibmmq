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

1. Login to your IBM Cloud account and access the IBM Cloud Shell and execute oc login

2. Download the yaml file
```
wget https://raw.githubusercontent.com/filemancp4i/ibmmq/main/demo-mq.yaml
```

3. Create the TLS cert/key. Two files (tls.crt and tls.key) are created by openssl.
```
openssl req -newkey rsa:2048 -nodes -keyout tls.key -subj "/CN=localhost" -x509 -days 3650 -out tls.crt
```


4. Execute the following commands to encode the certificate and key. 
```
base64 tls.crt -w 0
```
```
base64 tls.key -w 0
```

5. Update demo-mq.yaml file (line 36 and 37) with the output from step 4. 
```
vi demo-mq.yaml
```

6. Apply yaml file to OpenShift to create a QueueManager
```
oc apply -f demo-mq.yaml
```

7. (optional, not part of the lab) Execute this script to create key store. This keystore file will be used by client applications to establish TLS secured connection to MQ.
```
keytool -import -trustcacerts -alias democrt -file tls.crt -keystore /your/local/file/system/demo.jks
```
