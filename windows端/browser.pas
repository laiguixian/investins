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
const interneturl='http://207.148.17.218:8096/gethost?webname=itdknet';//获取internet国内实际地址
var
  browserForm: TbrowserForm;
  internetip:String;
  intergetfinish:boolean;
  tryapi:string;
  finaurl:string;//最终打开的地址

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
    //不用检查 WOW64 ，操作系统自动完成上述的区别。
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
  IEEmulator(11000);//使内嵌浏览器支持ie11
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
      showmessage('网上'+internetip+'和本地192.168.1.66的服务都没有办法连上');
      internetip:='';
      application.Terminate;
    end;
  end;
  if(length(internetip)>0)then
  begin
    browserform.Caption:='投资工具:'+internetip;
    finaurl := 'http://' + internetip + ':8095/whtzjk';
    //webbrowser1.Silent:=true;

    webbrowser1.Navigate(finaurl);
  end;
end;

end.
