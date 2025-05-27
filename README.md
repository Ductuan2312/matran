# Ma Trận - Ứng Dụng Tính Toán Ma Trận Toàn Diện

![Ma Trận Logo](https://github.com/Ductuan2312/matran/blob/834f054e311249bbb15c01f5273798f4f9c9ea67/app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp)

## Tổng Quan

**Ma Trận** là một ứng dụng Android mạnh mẽ và trực quan, được thiết kế để hỗ trợ tính toán ma trận phức tạp với giao diện người dùng tinh tế. Ứng dụng tích hợp các thuật toán đại số tuyến tính hiện đại, cùng với hiển thị chi tiết các bước tính toán, giúp người dùng không chỉ nhận kết quả mà còn hiểu rõ quá trình tính toán.
đã có trên APKPure:https://apkpure.com/p/com.example.matran
trang giới thiệu các cá nhân và các app phát triển:https://ductuan.studio/
## Tính Năng Nổi Bật

### 1. Phép Toán Ma Trận Đầy Đủ
- **Phép toán cơ bản:** Cộng, trừ, nhân ma trận với giao diện trực quan
- **Phép toán nâng cao:** Tính định thức, ma trận nghịch đảo, chuyển vị, hạng ma trận
- **Phân tích ma trận:** SVD (Singular Value Decomposition), tính trị riêng (Eigenvalues)
- **Ứng dụng thực tế:** Giải hệ phương trình tuyến tính, tính tích chập (Convolution)

### 2. Trực Quan Hóa Quy Trình
- **Hiển thị từng bước tính toán:** Chi tiết quá trình thực hiện giúp người dùng hiểu sâu sắc các phép toán
- **Giao diện tab:** Chuyển đổi dễ dàng giữa kết quả cuối cùng và các bước trung gian
- **Kết quả được định dạng rõ ràng:** Ma trận kết quả được hiển thị một cách chuyên nghiệp và dễ đọc

### 3. Thiết Kế Hướng Người Dùng
- **Material Design:** Giao diện hiện đại tuân theo nguyên tắc thiết kế của Google
- **Hỗ trợ tiếng Việt:** Giao diện và hướng dẫn được Việt hóa hoàn toàn
- **Điều hướng trực quan:** Bottom navigation và toolbar giúp điều hướng dễ dàng
- **Thông báo lỗi thông minh:** Phát hiện và thông báo lỗi với hướng dẫn khắc phục

### 4. Quản Lý Lịch Sử Tính Toán
- **Lưu trữ thông minh:** Tự động lưu các phép tính gần đây để dễ dàng tham khảo
- **Truy cập nhanh:** Xem lại các tính toán trước đó chỉ với một chạm
- **Chia sẻ kết quả:** Xuất và chia sẻ kết quả tính toán qua các ứng dụng khác

### 5. Phần Lý Thuyết
- **Tích hợp kiến thức:** Phần lý thuyết giải thích các khái niệm đại số tuyến tính
- **Dạng expandable list:** Dễ dàng mở rộng để đọc thêm thông tin chi tiết
- **Công thức toán học:** Trình bày các công thức quan trọng một cách chuyên nghiệp

## Công Nghệ Sử Dụng

- **Kiến trúc MVC:** Tổ chức mã nguồn rõ ràng với sự phân tách giữa Model, View và Controller
- **Java Core:** Sử dụng Java thuần để tính toán đảm bảo hiệu suất tối ưu
- **Material Components:** Áp dụng các thành phần Material Design hiện đại
- **SharedPreferences:** Lưu trữ lịch sử phép tính hiệu quả
- **Serializable:** Truyền dữ liệu phức tạp giữa các Activity
- **Gson:** Chuyển đổi đối tượng Java thành JSON để lưu trữ

## Hướng Dẫn Sử Dụng

### Thực Hiện Phép Tính Ma Trận
1. Từ màn hình chính, chọn phép tính mong muốn (Cộng, Trừ, Nhân, v.v.)
2. Nhập kích thước ma trận và nhấn "Áp dụng"
3. Nhập các giá trị cho ma trận (hoặc các ma trận)
4. Nhấn nút "Tính toán" để xem kết quả
5. Chuyển đổi giữa tab "KẾT QUẢ" và "CÁC BƯỚC" để xem chi tiết tính toán

### Xem Lịch Sử
1. Mở tab "Lịch sử" từ thanh điều hướng dưới cùng
2. Nhấn vào bất kỳ phép tính nào để xem lại chi tiết
3. Sử dụng nút xóa để làm mới lịch sử nếu cần

### Xem Lý Thuyết
1. Chọn tab "Lý thuyết" từ thanh điều hướng
2. Mở rộng các mục để đọc giải thích chi tiết về các khái niệm ma trận

## Ưu Điểm Vượt Trội

- **Hiệu suất cao:** Tối ưu hóa thuật toán đảm bảo tính toán nhanh chóng ngay cả với ma trận kích thước lớn
- **Chính xác:** Sử dụng kiểu dữ liệu double cho độ chính xác cao trong tính toán
- **Tiết kiệm tài nguyên:** Thiết kế hiệu quả giảm thiểu sử dụng bộ nhớ
- **Mã nguồn có tổ chức:** Cấu trúc rõ ràng, dễ bảo trì và mở rộng
- **Trải nghiệm người dùng liền mạch:** Giao diện trực quan, phản hồi nhanh chóng

## Triển Vọng Phát Triển

- Tích hợp thêm phép toán nâng cao như phân tích QR, LU, Cholesky
- Hỗ trợ vẽ đồ thị biểu diễn trực quan kết quả
- Chế độ tối (Dark mode) thân thiện với người dùng
- Tích hợp trí tuệ nhân tạo để gợi ý giải pháp tối ưu
- Phát triển phiên bản web với công nghệ WebAssembly

## Kết Luận

**Ma Trận** không chỉ là một ứng dụng tính toán đơn thuần mà còn là một công cụ giáo dục hiệu quả, giúp người dùng hiểu sâu sắc về đại số tuyến tính. Với sự kết hợp giữa giao diện thân thiện và thuật toán mạnh mẽ, ứng dụng đáp ứng nhu cầu của cả sinh viên, giáo viên và chuyên gia làm việc với ma trận.

---

## Thông Tin Liên Hệ

**Tác giả:**Đức Tuân 
**Email:** tuangato147@gmail.com  
**GitHub:** [github.com/Ductuan2312](https://github.com/Ductuan2312)

---

*Phát triển với ♥ bởi Đức Tuân - 2025*
