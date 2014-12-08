@echo Note: instructions for properly updating the variables are in the end of the file
@echo The contents here may just be copied into cmd.exe or some other shell (just note that 
@echo in some cases a call to git may stop executing, so, you may need to copy the commands in chunks). 

set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_27
set ECLIPSE_CLEAN=E:\eclipse\eclipse-SDK-3.7.1-win32\eclipse

set BUILDDIR=%~dp0
set BRANCH=development

cd /d %BUILDDIR%
cd /d ../../build

set BUILD_DIR=%BUILDDIR%..\..\build\src
set DEPLOY_DIR=%BUILDDIR%..\..\build\deploy
set FASTCOPY=%BUILDDIR%..\..\tool\ha_FastCopy2.11
set ANT_HOME=%BUILDDIR%..\..\tool\apache-ant-1.8.2

set KEYSTORE=%DEPLOY_DIR%\opsdevkeystore
set KEYSTORE_ALIAS=opsdev
set STOREPASS=key123

set LAUNCHER_PLUGIN=org.eclipse.equinox.launcher_1.2.0.v20110502
set BUILDER_PLUGIN=org.eclipse.pde.build_3.7.0.v20110512-1320

set BASEOS=win32
set BASEWS=win32
set BASEARCH=x86

set PATH=
set PATH=C:\Python27;%PATH%
set PATH=%FASTCOPY%;%PATH%
set PATH=%JAVA_HOME%\bin;%PATH%
set PATH=%ANT_HOME%\bin;%PATH%


@echo actual build command
mkdir %BUILD_DIR%
mkdir %DEPLOY_DIR%

REM Ã· æ√‹¬Î«Î ‰»Îkey123
keytool -genkey -dname "CN=hauwei software, OU=vrp, O=huawei, L=Florianopolis, ST=SC, C=xielequan" -keystore %DEPLOY_DIR%\opsdevkeystore -alias opsdev -validity 3650
keytool -selfcert -alias opsdev -keystore %DEPLOY_DIR%\opsdevkeystore -validity 3650
keytool -export -keystore %DEPLOY_DIR%\opsdevkeystore -alias opsdev -file opsdev_certificate.cer
