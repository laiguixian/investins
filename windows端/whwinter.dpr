program whwinter;

uses
  Vcl.Forms,
  browser in 'browser.pas' {browserForm};

{$R *.res}

begin
  Application.Initialize;
  Application.MainFormOnTaskbar := True;
  Application.CreateForm(TbrowserForm, browserForm);
  Application.Run;
end.
