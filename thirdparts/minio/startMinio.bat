@echo off
set MINIO_ROOT_USER=admin
set MINIO_ROOT_PASSWORD=12345678
minio.exe server data --console-address :10211  --address :10212
pause;