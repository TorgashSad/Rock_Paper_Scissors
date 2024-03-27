A 'Rock, Paper & Scissors' game!

Project is a Spring application for IntelliJ IDEA (or you can just run RpsApplication class)

Connection by Telnet: 
1) for Windows, for example, turn on [Telnet client feature for windows](https://forums.ivanti.com/s/article/unable-to-Telnet-the-server-due-to-telnet-is-not-recognized-as-a-internal-or-external-command-on-Windows-7-when-trying-to-verify-connection-to-GoldSync-Server?language=en_US);

2) Open Windows PowerShell or Command line

3) Enter ```telnet *server ip* *server port```. Server port is customizable in src/main/resources/application.properties.

4) Wait for an opponent and enjoy the epic multiplayer experience!

DEV Note:
'master' branch versions create a new Thread for each client
"threadPool' branch version utilizes Java 21 feature of virtual threads by using an ExecutorService returned by Executors.newVirtualThreadPerTaskExecutor();

P. S. there are also some integration tests in src/test/java/com/example/rps/RpsApplicationTests.java
