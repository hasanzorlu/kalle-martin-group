REM echo off

java -cp ../lib/boot.jar -Xms16m -Xmx128m madkit.boot.Madkit madkit.kernel.Booter --config configs/remoteconfig.cfg
