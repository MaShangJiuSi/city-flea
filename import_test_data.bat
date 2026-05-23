@echo off
echo Importing test data...
chcp 65001 >nul
mysql -u root -proot city_flea -e "SET NAMES utf8mb4; SOURCE d:/code/MyProject/city-flea/docs/sql/test_data.sql;"
if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo ✅ Test data imported successfully!
    echo ========================================
    echo.
    echo Verifying data...
    mysql -u root -proot city_flea -e "SELECT 'Employee Count' as 'Item', COUNT(*) as 'Count' FROM employee UNION ALL SELECT 'User Count', COUNT(*) FROM user UNION ALL SELECT 'Goods Count(Approved)', COUNT(*) FROM goods WHERE goods_status = 1 UNION ALL SELECT 'Order Count', COUNT(*) FROM orders UNION ALL SELECT 'Category Count', COUNT(*) FROM category;"
) else (
    echo.
    echo ========================================
    echo ❌ Import failed!
    echo ========================================
)
pause
