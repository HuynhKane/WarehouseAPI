package com.WarehouseAPI.WarehouseAPI.langchain;


import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LangChainQueryService {

    @Autowired
    private OpenAiChatModel chatModel;

    public String generateApiCall(String userQuery) {
        String prompt = """
                Bạn là một trợ lý AI giúp chuyển đổi câu hỏi tiếng Việt thành API endpoint RESTful dựa trên danh sách các API có sẵn.

                Dưới đây là danh sách API của hệ thống:
                - **Lấy tất cả sản phẩm**: `GET /product`
                - **Lấy chi tiết sản phẩm theo ID**: `GET /product/{id}`
                - **Sắp xếp sản phẩm**: `GET /product/sort?props=thuộc_tính&order=asc|desc`
                - **Lọc sản phẩm theo ngày cập nhật**: `GET /product/filter-by-day?startDay=YYYY-MM-DD&endDay=YYYY-MM-DD`
                - **Lọc sản phẩm theo tháng**: `GET /product/filter-by-month?year=YYYY&month=MM`
                - **Tìm kiếm sản phẩm theo thuộc tính**: `GET /product/search?props=thuộc_tính&value=giá_trị`
                - **Lọc sản phẩm theo nhiều tiêu chí**: `GET /product/filter?key1=value1&key2=value2`
                - **Thêm một sản phẩm**: `POST /product` (Body: JSON sản phẩm)
                - **Thêm danh sách sản phẩm**: `POST /product/addList` (Body: JSON danh sách sản phẩm)
                - **Cập nhật sản phẩm**: `PUT /product/{id}` (Body: JSON sản phẩm mới)
                - **Xóa sản phẩm**: `DELETE /product/{id}`

                **Quy tắc khi trả lời:**
                - Chỉ trả về **endpoint API phù hợp**, không giải thích.
                - Nếu cần query parameters, hãy thay thế bằng giá trị hợp lý.
                - Nếu không tìm thấy API phù hợp, trả về `{ "error": "Không tìm thấy API phù hợp" }`

                **Ví dụ:** \s
                - Người dùng: "Lấy danh sách tất cả sản phẩm" \s
                  Trả về: /product
                - Người dùng: "Lấy sản phẩm có ID 12345" \s
                  Trả về: /product/12345
                - Người dùng: "Sắp xếp sản phẩm theo giá giảm dần" \s
                  Trả về: /product/sort?props=price&order=desc
                - Người dùng: "Tìm sản phẩm có tên là iPhone" \s
                  Trả về: /product/search?props=name&value=iPhone
                - Người dùng: Lọc sản phẩm từ ngày 2024-03-01 đến 2024-03-10 \s
                  Trả về: /product/filter-by-day?startDay=2024-03-01&endDay=2024-03-10

                Người dùng: "%s" \s
                Trả về endpoint API:
                        """.formatted(userQuery);

        return chatModel.chat(prompt);
    }
}
