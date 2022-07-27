# IBM Workshop Guide for **MQ on CP4I**

## Overview  

<!--- cSpell:ignore gitorg YAMLs -->

The guide is to help users to set up IBM MQ on Cloud after being installed. Only simple use case is included.

To be more specific, the following items are included:

-   Queue Manager
-   ConfigMap
-   Secret
-   Route

```
openssl req -newkey rsa:2048 -nodes -keyout tls.key -subj "/CN=localhost" -x509 -days 3650 -out tls.crt
```

```
oc apply -f demo-mq.yaml
```
