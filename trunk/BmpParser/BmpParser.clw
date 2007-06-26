; CLW file contains information for the MFC ClassWizard

[General Info]
Version=1
LastClass=CBmpParserDlg
LastTemplate=CDialog
NewFileInclude1=#include "stdafx.h"
NewFileInclude2=#include "BmpParser.h"

ClassCount=3
Class1=CBmpParserApp
Class2=CBmpParserDlg
Class3=CAboutDlg

ResourceCount=3
Resource1=IDD_ABOUTBOX
Resource2=IDR_MAINFRAME
Resource3=IDD_BMPPARSER_DIALOG

[CLS:CBmpParserApp]
Type=0
HeaderFile=BmpParser.h
ImplementationFile=BmpParser.cpp
Filter=N

[CLS:CBmpParserDlg]
Type=0
HeaderFile=BmpParserDlg.h
ImplementationFile=BmpParserDlg.cpp
Filter=D
BaseClass=CDialog
VirtualFilter=dWC
LastObject=IDC_PIC

[CLS:CAboutDlg]
Type=0
HeaderFile=BmpParserDlg.h
ImplementationFile=BmpParserDlg.cpp
Filter=D

[DLG:IDD_ABOUTBOX]
Type=1
Class=CAboutDlg
ControlCount=4
Control1=IDC_STATIC,static,1342177283
Control2=IDC_STATIC,static,1342308480
Control3=IDC_STATIC,static,1342308352
Control4=IDOK,button,1342373889

[DLG:IDD_BMPPARSER_DIALOG]
Type=1
Class=CBmpParserDlg
ControlCount=8
Control1=ID_IMPORT_FILE,button,1342242817
Control2=IDCANCEL,button,1342242816
Control3=IDC_PIC,static,1342177287
Control4=ID_EXPORT_FILE,button,1342242817
Control5=IDC_OPEN_DB_FILE,button,1342242816
Control6=IDC_STATIC,static,1342308352
Control7=IDC_CMB_CLUSTER,combobox,1344340226
Control8=IDC_STATIC,static,1342308352

