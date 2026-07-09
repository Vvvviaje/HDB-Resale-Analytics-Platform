package com.uraapi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 对应 data.gov.sg 上 "Resale flat prices" 数据集里的一条记录。
 * 字段名基本照抄官方数据字典：month / town / flat_type / block / street_name /
 * storey_range / floor_area_sqm / flat_model / lease_commence_date / remaining_lease / resale_price
 */
@Entity
@Table(name = "resale_transaction")
public class ResaleTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // data.gov.sg 每条记录自带的唯一编号（CKAN 的 _id），用来防止重复抓取时插入重复数据
    @Column(unique = true)
    private Long sourceId;

    // 注意这里加了 @Column(name = "txn_month")：
    // H2 数据库把 "month" 当成自己的内置关键字（月份函数），直接拿来当列名会报语法错误，
    // 所以让 Java 字段还是叫 month，但存到数据库里的时候用 txn_month 这个名字，避开冲突。
    @Column(name = "txn_month")
    private String month;          // 例如 "2024-06"

    private String town;           // 例如 "ANG MO KIO"
    private String flatType;       // 例如 "3 ROOM"
    private String block;
    private String streetName;
    private String storeyRange;    // 例如 "04 TO 06"
    private Double floorAreaSqm;
    private String flatModel;
    private String leaseCommenceDate;
    private String remainingLease;
    private Double resalePrice;

    // 记录这条数据是什么时候被我们抓进库里的，方便后面做"当天新增交易"的 Merkle Tree
    private LocalDateTime fetchedAt;

    public ResaleTransaction() {
    }

    // ---- getters & setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStoreyRange() {
        return storeyRange;
    }

    public void setStoreyRange(String storeyRange) {
        this.storeyRange = storeyRange;
    }

    public Double getFloorAreaSqm() {
        return floorAreaSqm;
    }

    public void setFloorAreaSqm(Double floorAreaSqm) {
        this.floorAreaSqm = floorAreaSqm;
    }

    public String getFlatModel() {
        return flatModel;
    }

    public void setFlatModel(String flatModel) {
        this.flatModel = flatModel;
    }

    public String getLeaseCommenceDate() {
        return leaseCommenceDate;
    }

    public void setLeaseCommenceDate(String leaseCommenceDate) {
        this.leaseCommenceDate = leaseCommenceDate;
    }

    public String getRemainingLease() {
        return remainingLease;
    }

    public void setRemainingLease(String remainingLease) {
        this.remainingLease = remainingLease;
    }

    public Double getResalePrice() {
        return resalePrice;
    }

    public void setResalePrice(Double resalePrice) {
        this.resalePrice = resalePrice;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
}