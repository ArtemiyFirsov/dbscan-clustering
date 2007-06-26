// BmpParserDlg.h : header file
//

#if !defined(AFX_BMPPARSERDLG_H__F16C7F90_3DCA_4D79_B913_F094FA80D31F__INCLUDED_)
#define AFX_BMPPARSERDLG_H__F16C7F90_3DCA_4D79_B913_F094FA80D31F__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

/////////////////////////////////////////////////////////////////////////////
// CBmpParserDlg dialog

#define DB_FILE_W_H       "%u\t%u\r\n"
#define DB_FILE_HEADER		"PID\tx\ty\tr\tg\tb\tcls\r\n"
#define DB_FILE_RECORD		"%u\t%u\t%u\t%u\t%u\t%u\t%d\r\n"

class CBmpParserDlg : public CDialog
{
// Construction
public:
	CBmpParserDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	//{{AFX_DATA(CBmpParserDlg)
	enum { IDD = IDD_BMPPARSER_DIALOG };
	CComboBox	m_cmb_cluster;
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CBmpParserDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	HICON m_hIcon;

	void LoadBitMapFromFile(CString bitmapFile);
	void SaveBitMapToFile(char *bitmapFile);

	void LoadBitMapFromDB(CString cls);
	void SaveBitMapToDB(CString dbFile);

	void doRefreshFromDB(CString dbFile,int cluster);

	// Generated message map functions
	//{{AFX_MSG(CBmpParserDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnImportFile();
	afx_msg void OnExportFile();
	afx_msg void OnOpenDbFile();
	afx_msg void OnBtnRefresh();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_BMPPARSERDLG_H__F16C7F90_3DCA_4D79_B913_F094FA80D31F__INCLUDED_)
