package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.dto.MonthlyCost;
import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface ImportPackageRepos extends MongoRepository<ImportPackage, String> {
    @Aggregation(pipeline = {
            "{ $match: { statusDone: 'APPROVED', importDate: { $gte: ?0, $lt: ?1 } } }",

            // Join product theo listProducts
            "{ $lookup: { from: 'product', localField: 'listProducts', foreignField: '_id', as: 'productList' } }",

            // Tách từng sản phẩm
            "{ $unwind: '$productList' }",

            // Tính: importPrice * quantity
            "{ $addFields: { cost: { $multiply: [ { $toDouble: '$productList.importPrice' }, '$productList.quantity' ] } } }",

            // Group theo tháng
            "{ $group: { _id: { month: { $month: '$importDate' } }, totalCost: { $sum: '$cost' } } }",

            // Format kết quả
            "{ $project: { _id: 0, month: '$_id.month', totalCost: 1 } }",

            // Sort theo tháng
            "{ $sort: { month: 1 } }"
    })
    List<MonthlyCost> getMonthlyCostByYear(Date startDate, Date endDate);

}

