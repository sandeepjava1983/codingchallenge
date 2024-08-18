# codingchallenge
Coding challenge points which can be improved.

Security:
Currently no API is secured and can be accessed directly. Which can be stopped by using Oauth2 and Okta.
No API gateway and Eureka discovery is used. This can be added as a important design pattern for a single entry point (API gateway).
From where the Authentication can be get triggered.
Vaults can be used to store confidential information like username and passoword of db.

Validation :
Validator framework need to be added which will help to pass only valid requests to service layer.

Verification:
OTP service can be get added to make transaction as a TWO factor Authentication.

Communication to account holders can be get extended to SMS along with Email service.

Technically,
Splunk can be added to monitor the logs.
or tools like Zipkin Sleuth can be get added for distrubted tracing.

ControllerAdvice can be get used for centralized exception handling.

Circuit breaker patterns can be used for fault tolerance.

Config server can be used for central config.

Outside bank transactions can be get extended for Inward and Outward clearing of the accounts.

If big file transactions are getting uploaded we can use S3 bucket to store some files like transaction files, reports.

Code should be kept enabled for horizontal scaling at any given point of time.

Tools like appdynamics can be used for performance monitoring.

Reporting tool can be get integrated with the system.

Once all above points will be get implemnted, to Make transactions more smoother and faster 
some brokers like Active MQ or KAFKA brokers can be get used.

ConcurrentHashMap is used for inmemory this can be replaced with Oracle DB and can be created replicas of same to avoid any natual disaster loss.