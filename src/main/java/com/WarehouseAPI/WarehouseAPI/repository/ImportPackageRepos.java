package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.dto.MonthlyCost;
import com.WarehouseAPI.WarehouseAPI.dto.PackageImportDetails;
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

    @Aggregation(pipeline = {
            // Bước 1: Lọc theo tháng và năm
            "{ $match: { statusDone: 'APPROVED', " +
                    "importDate: { $gte: ?0, $lt: ?1 } } }",

            // Bước 2: Join với collection product để lấy importPrice
            "{ $lookup: { from: 'product', localField: 'listProducts', foreignField: '_id', as: 'productList' } }",

            // Bước 3: Tách từng sản phẩm trong list
            "{ $unwind: '$productList' }",

            // Bước 4: Tính total import price cho mỗi package
            "{ $addFields: { totalImportPrice: { $multiply: [ { $toDouble: '$productList.importPrice' }, '$productList.quantity' ] } } }",

            // Bước 5: Group theo packageId và packageName
            "{ $group: { _id: { packageId: '$_id', packageName: '$packageName', importDate: '$importDate' }, totalImportPrice: { $sum: '$totalImportPrice' } } }",

            // Bước 6: Format kết quả
            "{ $project: { _id: 0, packageId: '$_id.packageId', packageName: '$_id.packageName',importDate: '$_id.importDate', totalImportPrice: 1 } }"
    })
    List<PackageImportDetails> getPackageImportDetailsByMonth(Date startDate, Date endDate);





}

