
set JAVA_HOME=d:\java\jdk1.4

set PATH=%JAVA_HOME%\bin;%PATH%

java -version

set classpath=%classpath%;hiena.player.jar;
set classpath=%classpath%;vorbisspi0.8.jar;
set classpath=%classpath%;jl0.4.jar;
set classpath=%classpath%;jogg-0.0.5.jar;
set classpath=%classpath%;jorbis-0.0.12.jar;
set classpath=%classpath%;mp3spi1.8.jar;
set classpath=%classpath%;tritonus_share.jar;
set classpath=%classpath%;jid3.jar;

rem java -jar hiena.player.jar
java org.roussev.hiena.MainWindow

pause