@echo Note: instructions for properly updating the variables are in the end of the file
@echo The contents here may just be copied into cmd.exe or some other shell (just note that 
@echo in some cases a call to git may stop executing, so, you may need to copy the commands in chunks). 

REM set JAVA_HOME=D:\Program Files\Java\jdk1.6.0_27
set ECLIPSE_CLEAN=E:\eclipse\eclipse-SDK-3.7\eclipse-SDK-3.7.1-win32\eclipse

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
set PATH=%ANT_HOME%\bin;%PATH%;%SystemRoot%\system32;

@echo actual build command
mkdir %BUILD_DIR%
mkdir %DEPLOY_DIR%

rmdir /S/Q  %BUILD_DIR%\Pydev\features
rmdir /S/Q  %BUILD_DIR%\Pydev\plugins
mkdir %BUILD_DIR%\Pydev\features
mkdir %BUILD_DIR%\Pydev\plugins
xcopy /E/Q/Y %BUILDDIR%..\..\features %BUILD_DIR%\Pydev\features
xcopy /E/Q/Y %BUILDDIR%..\..\plugins %BUILD_DIR%\Pydev\plugins

cd /d %BUILD_DIR%
cd Pydev


del %DEPLOY_DIR%/Pydev/* /Y

@echo If copied/pasted into cmd.exe, it will break here
@echo to clean after the build: -DcleanAfter.set=true

cd /d %BUILDDIR%
ant -DbuildDirectory=%BUILD_DIR%\Pydev -Ddeploy.dir=%DEPLOY_DIR%\Pydev -Dvanilla.eclipse=%ECLIPSE_CLEAN%  -DKEYSTORE=%KEYSTORE% -DSTOREPASS=%STOREPASS%  -Dlocal_build=true -Dlauncher.plugin=%LAUNCHER_PLUGIN% -Dbuilder.plugin=%BUILDER_PLUGIN% -Dbaseos=%BASEOS% -Dbasews=%BASEWS% -Dbasearch=%BASEARCH% -DKEYSTORE_ALIAS=%KEYSTORE_ALIAS% 

 