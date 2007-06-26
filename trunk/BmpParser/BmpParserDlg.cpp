// BmpParserDlg.cpp : implementation file
//

#include "stdafx.h"
#include "BmpParser.h"
#include "BmpParserDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

HBITMAP m_hBitmap = NULL;
CString m_dbFile = "default.db";
CString DBFileName;

void errhandler(CString msg,HWND hdl){
	MessageBox(hdl,msg,"errhandler",IDOK);
}

PBITMAPINFO CreateBitmapInfoStruct(HWND hwnd, HBITMAP hBmp)
{ 
    BITMAP bmp; 
    PBITMAPINFO pbmi; 
    WORD    cClrBits; 

    // Retrieve the bitmap color format, width, and height. 
    if (!GetObject(hBmp, sizeof(BITMAP), (LPSTR)&bmp)) 
        errhandler("GetObject", hwnd); 

    // Convert the color format to a count of bits. 
    cClrBits = (WORD)(bmp.bmPlanes * bmp.bmBitsPixel); 
    if (cClrBits == 1) 
        cClrBits = 1; 
    else if (cClrBits <= 4) 
        cClrBits = 4; 
    else if (cClrBits <= 8) 
        cClrBits = 8; 
    else if (cClrBits <= 16) 
        cClrBits = 16; 
    else if (cClrBits <= 24) 
        cClrBits = 24; 
    else cClrBits = 32; 

    // Allocate memory for the BITMAPINFO structure. (This structure 
    // contains a BITMAPINFOHEADER structure and an array of RGBQUAD 
    // data structures.) 

     if (cClrBits != 24) 
         pbmi = (PBITMAPINFO) LocalAlloc(LPTR, 
                    sizeof(BITMAPINFOHEADER) + 
                    sizeof(RGBQUAD) * (1<< cClrBits)); 

     // There is no RGBQUAD array for the 24-bit-per-pixel format. 

     else 
         pbmi = (PBITMAPINFO) LocalAlloc(LPTR, 
                    sizeof(BITMAPINFOHEADER)); 

    // Initialize the fields in the BITMAPINFO structure. 

    pbmi->bmiHeader.biSize = sizeof(BITMAPINFOHEADER); 
    pbmi->bmiHeader.biWidth = bmp.bmWidth; 
    pbmi->bmiHeader.biHeight = bmp.bmHeight; 
    pbmi->bmiHeader.biPlanes = bmp.bmPlanes; 
    pbmi->bmiHeader.biBitCount = bmp.bmBitsPixel; 
    if (cClrBits < 24) 
        pbmi->bmiHeader.biClrUsed = (1<<cClrBits); 

    // If the bitmap is not compressed, set the BI_RGB flag. 
    pbmi->bmiHeader.biCompression = BI_RGB; 

    // Compute the number of bytes in the array of color 
    // indices and store the result in biSizeImage. 
    // For Windows NT, the width must be DWORD aligned unless 
    // the bitmap is RLE compressed. This example shows this. 
    // For Windows 95/98/Me, the width must be WORD aligned unless the 
    // bitmap is RLE compressed.
    pbmi->bmiHeader.biSizeImage = ((pbmi->bmiHeader.biWidth * cClrBits +31) & ~31) /8
                                  * pbmi->bmiHeader.biHeight; 
    // Set biClrImportant to 0, indicating that all of the 
    // device colors are important. 
     pbmi->bmiHeader.biClrImportant = 0; 
     return pbmi; 
 } 

void CreateBMPFile(HWND hwnd, LPTSTR pszFile, PBITMAPINFO pbi, 
                  HBITMAP hBMP, HDC hDC) 
 { 
     HANDLE hf;                 // file handle 
    BITMAPFILEHEADER hdr;       // bitmap file-header 
    PBITMAPINFOHEADER pbih;     // bitmap info-header 
    LPBYTE lpBits;              // memory pointer 
    DWORD dwTotal;              // total count of bytes 
    DWORD cb;                   // incremental count of bytes 
    BYTE *hp;                   // byte pointer 
    DWORD dwTmp; 

    pbih = (PBITMAPINFOHEADER) pbi; 
    lpBits = (LPBYTE) GlobalAlloc(GMEM_FIXED, pbih->biSizeImage);

    if (!lpBits) 
         errhandler("GlobalAlloc", hwnd); 

    // Retrieve the color table (RGBQUAD array) and the bits 
    // (array of palette indices) from the DIB. 
    if (!GetDIBits(hDC, hBMP, 0, (WORD) pbih->biHeight, lpBits, pbi, 
        DIB_RGB_COLORS)) 
    {
        errhandler("GetDIBits", hwnd); 
    }

    // Create the .BMP file. 
    hf = CreateFile(pszFile, 
                   GENERIC_READ | GENERIC_WRITE, 
                   (DWORD) 0, 
                    NULL, 
                   CREATE_ALWAYS, 
                   FILE_ATTRIBUTE_NORMAL, 
                   (HANDLE) NULL); 
    if (hf == INVALID_HANDLE_VALUE) 
        errhandler("CreateFile", hwnd); 
    hdr.bfType = 0x4d42;        // 0x42 = "B" 0x4d = "M" 
    // Compute the size of the entire file. 
    hdr.bfSize = (DWORD) (sizeof(BITMAPFILEHEADER) + 
                 pbih->biSize + pbih->biClrUsed 
                 * sizeof(RGBQUAD) + pbih->biSizeImage); 
    hdr.bfReserved1 = 0; 
    hdr.bfReserved2 = 0; 

    // Compute the offset to the array of color indices. 
    hdr.bfOffBits = (DWORD) sizeof(BITMAPFILEHEADER) + 
                    pbih->biSize + pbih->biClrUsed 
                    * sizeof (RGBQUAD); 

    // Copy the BITMAPFILEHEADER into the .BMP file. 
    if (!WriteFile(hf, (LPVOID) &hdr, sizeof(BITMAPFILEHEADER), 
        (LPDWORD) &dwTmp,  NULL)) 
    {
       errhandler("WriteFile", hwnd); 
    }

    // Copy the BITMAPINFOHEADER and RGBQUAD array into the file. 
    if (!WriteFile(hf, (LPVOID) pbih, sizeof(BITMAPINFOHEADER) 
                  + pbih->biClrUsed * sizeof (RGBQUAD), 
                  (LPDWORD) &dwTmp, ( NULL)))
        errhandler("WriteFile", hwnd); 

    // Copy the array of color indices into the .BMP file. 
    dwTotal = cb = pbih->biSizeImage; 
    hp = lpBits; 
    if (!WriteFile(hf, (LPSTR) hp, (int) cb, (LPDWORD) &dwTmp,NULL)) 
           errhandler("WriteFile", hwnd); 

    // Close the .BMP file. 
     if (!CloseHandle(hf)) 
           errhandler("CloseHandle", hwnd); 

    // Free memory. 
    GlobalFree((HGLOBAL)lpBits);
}





class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
		// No message handlers
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CBmpParserDlg dialog

CBmpParserDlg::CBmpParserDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CBmpParserDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CBmpParserDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CBmpParserDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CBmpParserDlg)
	DDX_Control(pDX, IDC_CMB_CLUSTER, m_cmb_cluster);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CBmpParserDlg, CDialog)
	//{{AFX_MSG_MAP(CBmpParserDlg)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(ID_IMPORT_FILE, OnImportFile)
	ON_BN_CLICKED(ID_EXPORT_FILE, OnExportFile)
	ON_BN_CLICKED(IDC_OPEN_DB_FILE, OnOpenDbFile)
	ON_BN_CLICKED(IDC_BTN_REFRESH, OnBtnRefresh)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CBmpParserDlg message handlers

BOOL CBmpParserDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	// TODO: Add extra initialization here
	
	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CBmpParserDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CBmpParserDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CBmpParserDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}

void CBmpParserDlg::OnImportFile() 
{
	CString		ImportFileName = "";
	CFileDialog ImportFileDlg(TRUE, NULL, NULL, NULL, _T("位图文件 (*.bmp)|*.bmp||"), NULL);
	ImportFileDlg.m_ofn.lpstrTitle = _T("载入位图文件");

	if(ImportFileDlg.DoModal() == IDOK){
		ImportFileName = ImportFileDlg.GetPathName();
		LoadBitMapFromFile(ImportFileName);
	}
	
	if(ImportFileName != ""){
			CFileDialog dbFileDlg(FALSE, NULL, NULL, NULL, _T("数据文件 (*.dbx)|*.dbx||"), NULL);
		dbFileDlg.m_ofn.lpstrTitle = _T("输出到数据文件");
		
		if(dbFileDlg.DoModal() == IDOK){
			m_dbFile = dbFileDlg.GetPathName();
			m_dbFile = m_dbFile + ".dbx";
			SaveBitMapToDB(m_dbFile);
			SetWindowText(m_dbFile);
		}
	}
}

void CBmpParserDlg::OnExportFile(){

	if( m_hBitmap == NULL)
		return;
	
	CString		ExportFileName = "";
	CFileDialog ExportFileDlg(FALSE, NULL, NULL, NULL, _T("位图文件 (*.bmp)|*.bmp||"), NULL);
	ExportFileDlg.m_ofn.lpstrTitle = _T("导出位图文件");

	if(ExportFileDlg.DoModal() == IDOK){
		ExportFileName = ExportFileDlg.GetPathName();
		SaveBitMapToFile(ExportFileName.GetBuffer(200));
	}
	

}


void CBmpParserDlg::LoadBitMapFromFile(CString bitmapFile){
	CWnd *hwnd;
	HDC hdc,hdcs;
	RECT rect;
	hwnd=GetDlgItem(IDC_PIC);//位图要显示的位置（picture控件）
	hdc=hwnd->GetDC()->m_hDC;
    hdcs = CreateCompatibleDC(hdc);
	
    BITMAP bm;
    m_hBitmap=(HBITMAP)::LoadImage(NULL,bitmapFile,IMAGE_BITMAP,0,0,LR_LOADFROMFILE|LR_CREATEDIBSECTION|LR_DEFAULTSIZE);
    GetObject(m_hBitmap, sizeof BITMAP, &bm);
	
	SelectObject(hdcs, m_hBitmap);
	
	hwnd->GetClientRect(&rect);
	::SetStretchBltMode(hdc,COLORONCOLOR);       
	::StretchBlt(hdc, rect.left, rect.top, rect.right, rect.bottom, hdcs, 0, 0, bm.bmWidth, bm.bmHeight,+SRCCOPY);
}
void CBmpParserDlg::SaveBitMapToFile(char* bitmapFile){
	CWnd *hwnd;
	HDC hdc;

	hwnd=GetDlgItem(IDC_PIC);//位图要显示的位置（picture控件）
	hdc=hwnd->GetDC()->m_hDC;
	PBITMAPINFO pBmpInfo = CreateBitmapInfoStruct(hwnd->m_hWnd,m_hBitmap);

	CreateBMPFile(hwnd->m_hWnd,bitmapFile,pBmpInfo,m_hBitmap,hdc);

}

void CBmpParserDlg::LoadBitMapFromDB(CString cls){


}
void CBmpParserDlg::SaveBitMapToDB(CString dbFile){

	int height = 0;
	int width = 0;
	CWnd *hwnd;
	HDC hdc;

	hwnd=GetDlgItem(IDC_PIC);//位图要显示的位置（picture控件）
	hdc=hwnd->GetDC()->m_hDC;
	PBITMAPINFO pBmpInfo = CreateBitmapInfoStruct(hwnd->m_hWnd,m_hBitmap);

	height = pBmpInfo->bmiHeader.biHeight;
	width = pBmpInfo->bmiHeader.biWidth;
	


	FILE *fp = fopen(dbFile,"wb");
	if(fp != NULL){
		ULONG ID = 0;
		//"ID\tx\ty\tr\tg\tb\tcls\r\n"
    fprintf(fp,DB_FILE_W_H,width,height);
		fprintf(fp,DB_FILE_HEADER);

		for(int h = 0;h < height;h++){
			for(int w = 0;w < width;w ++){
				COLORREF rgb = ::GetPixel(hdc,w,h);
				
				int r = GetRValue(rgb);
				int g = GetGValue(rgb);
				int b = GetBValue(rgb);
				char rowbuf[128] = {0x0};
				//"%u\t%u\t%u\t%u\t%u\t%u\t%d\r\n"
				fprintf(fp,DB_FILE_RECORD,ID++,w,h,r,g,b,-1);
			}
		}

		fclose(fp);			
	}
	
}



void CBmpParserDlg::doRefreshFromDB(CString dbFile,int cluster) 
{
		this->SetWindowText(dbFile);

		CWnd *hwnd;
		HDC hdc;

		hwnd=GetDlgItem(IDC_PIC);//位图要显示的位置（picture控件）
    hwnd->RedrawWindow();
		hdc=hwnd->GetDC()->m_hDC;

		FILE *fp = NULL;
		fp = fopen(dbFile,"rb");
		if(fp != NULL){
			char header[64] = {0x0};
			fgets(header,64,fp);
      fgets(header,64,fp);
			while(!feof(fp)){
				ULONG ID = 0;
				int w,h,r,g,b,cls;
				fscanf(fp,DB_FILE_RECORD,&ID,&w,&h,&r,&g,&b,&cls);
        char szCls[10] = {0x0};

				COLORREF color = 0;
        if(cluster == cls || cluster == -1){
          color = RGB(r,g,b);
          itoa(cls,szCls,10);
          if(m_cmb_cluster.FindString(0,szCls) == -1)
            m_cmb_cluster.AddString(szCls);
        }
				else
					color = RGB(255,255,255);
				::SetPixel(hdc,w,h,color);
			}
		}


}

void CBmpParserDlg::OnOpenDbFile() 
{	
	CFileDialog dbFileDlg(TRUE, NULL, NULL, NULL, _T("位图文件 (*.dbx)|*.dbx||"), NULL);
	dbFileDlg.m_ofn.lpstrTitle = _T("载入位图文件");

	if(dbFileDlg.DoModal() == IDOK){
		DBFileName = dbFileDlg.GetPathName();
    //int cur = m_cmb_cluster.GetCurSel()
    int cls = -1;
    doRefreshFromDB(DBFileName,cls);
	}
}

void CBmpParserDlg::OnBtnRefresh() 
{
	 int cls = -1;
   if(m_cmb_cluster.GetCount() > 0){
     
     CString strCls;
     m_cmb_cluster.GetWindowText(strCls);
     cls = atoi(strCls);
   }
   doRefreshFromDB(DBFileName,cls);
	
}
