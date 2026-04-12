$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot

if (Test-Path "out") {
    Remove-Item -Path "out" -Recurse -Force
}
New-Item -ItemType Directory -Path "out" | Out-Null

$dsFileJava = Get-ChildItem -Recurse -Filter "*.java" |
    Where-Object { $_.FullName -notmatch "\\out\\" } |
    ForEach-Object { $_.FullName }

if ($dsFileJava.Count -eq 0) {
    Write-Host "Khong tim thay file Java de bien dich."
    exit 1
}

Set-Content -Path "sources.txt" -Value $dsFileJava -Encoding ascii

javac -encoding UTF-8 -d out "@sources.txt"
if ($LASTEXITCODE -ne 0) {
    Write-Host "Bien dich that bai."
    exit $LASTEXITCODE
}

java -cp out app.ThuVienApp
