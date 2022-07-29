unit browser;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs,Registry, IdBaseComponent, IdComponent,
  IdTCPConnection, IdTCPClient, IdHTTP, Vcl.OleCtrls, SHDocVw;

type
  TbrowserForm = class(TForm)
    IdHTTP1: TIdHTTP;
    WebBrowser1: TWebBrowser;
    procedure FormShow(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;
const interneturl='http://207.148.17.218:8096/gethost?webname=itdknet';//��ȡinternet����ʵ�ʵ�ַ
var
  browserForm: TbrowserForm;
  internetip:String;
  intergetfinish:boolean;
  tryapi:string;
  finaurl:string;//���մ򿪵ĵ�ַ

implementation

{$R *.dfm}
// 11000 IE11
procedure IEEmulator(VerCode: Integer);
var
  RegObj: TRegistry;
begin
  RegObj := TRegistry.Create;
  try
    RegObj.RootKey := HKEY_CURRENT_USER;
    RegObj.Access := KEY_ALL_ACCESS;
    if not RegObj.OpenKey('\SOFTWARE\Microsoft\Internet ' +
        'Explorer\MAIN\FeatureControl\FEATURE_BROWSER_EMULATION', False) then exit;
    try
      RegObj.WriteInteger(ExtractFileName(ParamStr(0)), VerCode
        {10000 compatibility with IE10}); // for other versions read msdn

    finally
      RegObj.CloseKey;
    end;

    RegObj.RootKey := HKEY_LOCAL_MACHINE;
    RegObj.Access := KEY_ALL_ACCESS;
//    if IsWOW64 then
//    begin
//      if not RegObj.OpenKey('\SOFTWARE\Wow6432Node\Microsoft\Internet ' +
//        'Explorer\MAIN\FeatureControl\FEATURE_BROWSER_EMULATION', False) then exit;
//    end
//    else
    //���ü�� WOW64 ������ϵͳ�Զ��������������
    begin
      if not RegObj.OpenKey('\SOFTWARE\Microsoft\Internet ' +
        'Explorer\MAIN\FeatureControl\FEATURE_BROWSER_EMULATION', False) then exit;
    end;
    try
      RegObj.WriteInteger(ExtractFileName(ParamStr(0)), VerCode
        {10000 compatibility with IE10}); // for other versions read msdn

    finally
      RegObj.CloseKey;
    end;
  finally
    RegObj.Free;
  end;
end;

function getstrfromurl(url:string):string;
var
idhttp:tidhttp;
begin
  result:='';
  try
  idhttp:=tidhttp.Create(nil);
  idhttp.ConnectTimeout:=15000;
  idhttp.ReadTimeout:=15000;
  result:=trim(idhttp.Get(url));
  idhttp.Disconnect;
  idhttp.Free;
  except

  end;
end;

procedure TbrowserForm.FormShow(Sender: TObject);
begin
  browserForm.Left:=0;
  browserForm.Top:=0;
  browserForm.Width:=screen.width;
  browserForm.Height:=screen.Height;
  IEEmulator(11000);//ʹ��Ƕ�����֧��ie11
  internetip:=getstrfromurl(interneturl);
  if(length(internetip)>0)then
  begin
    internetip:=trim(internetip.substring(0,internetip.indexOf(':')));
    tryapi:='';
    tryapi := getstrfromurl('http://'+internetip+':8095/gethost?webname=itdknet');
    if(length(tryapi)=0)then
    begin
      internetip:='192.168.1.66';
    end;
  end
  else
  begin
    internetip:='192.168.1.66';
  end;
  if(internetip='192.168.1.66')then
  begin
    tryapi:='';
    tryapi := getstrfromurl('http://'+internetip+':8095/gethost?webname=itdknet');
    if(length(tryapi)=0)then
    begin
      showmessage('����'+internetip+'�ͱ���192.168.1.66�ķ���û�а취����');
      internetip:='';
      application.Terminate;
    end;
  end;
  if(length(internetip)>0)then
  begin
    browserform.Caption:='Ͷ�ʹ���:'+internetip;
    finaurl := 'http://' + internetip + ':8095/whtzjk';
    //webbrowser1.Silent:=true;

    webbrowser1.Navigate(finaurl);
  end;
end;

end.
