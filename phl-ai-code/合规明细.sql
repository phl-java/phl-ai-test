
    SELECT *,
    CASE
    WHEN tax_type_1_min_rate IS NULL OR taxBurdenRate_1 IS NULL OR taxBurdenRate_1 = 0 THEN 'no-risk'
    WHEN tax_type_1_min_rate = 0 AND COALESCE(tax_type_1_max_rate, 0) = 0 THEN 'no-risk'
    WHEN taxBurdenRate_1 >= tax_type_1_min_rate AND taxBurdenRate_1 <= COALESCE(tax_type_1_max_rate, tax_type_1_min_rate) THEN 'no-risk'
    WHEN taxBurdenRate_1 < tax_type_1_min_rate THEN
    CASE
    WHEN tax_type_1_min_rate = 0 THEN 'high'
    WHEN (tax_type_1_min_rate - taxBurdenRate_1) / tax_type_1_min_rate < 0.1 THEN 'low'
    WHEN (tax_type_1_min_rate - taxBurdenRate_1) / tax_type_1_min_rate <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    ELSE
    CASE
    WHEN COALESCE(tax_type_1_max_rate, tax_type_1_min_rate) = 0 THEN 'high'
    WHEN (taxBurdenRate_1 - COALESCE(tax_type_1_max_rate, tax_type_1_min_rate)) / COALESCE(tax_type_1_max_rate, tax_type_1_min_rate) < 0.1 THEN 'low'
    WHEN (taxBurdenRate_1 - COALESCE(tax_type_1_max_rate, tax_type_1_min_rate)) / COALESCE(tax_type_1_max_rate, tax_type_1_min_rate) <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    END AS risk_level_1,

    -- 税种2
    CASE
    WHEN tax_type_2_min_rate IS NULL OR taxBurdenRate_2 IS NULL OR taxBurdenRate_2 = 0 THEN 'no-risk'
    WHEN tax_type_2_min_rate = 0 AND COALESCE(tax_type_2_max_rate, 0) = 0 THEN 'no-risk'
    WHEN taxBurdenRate_2 >= tax_type_2_min_rate AND taxBurdenRate_2 <= COALESCE(tax_type_2_max_rate, tax_type_2_min_rate) THEN 'no-risk'
    WHEN taxBurdenRate_2 < tax_type_2_min_rate THEN
    CASE
    WHEN tax_type_2_min_rate = 0 THEN 'high'
    WHEN (tax_type_2_min_rate - taxBurdenRate_2) / tax_type_2_min_rate < 0.1 THEN 'low'
    WHEN (tax_type_2_min_rate - taxBurdenRate_2) / tax_type_2_min_rate <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    ELSE
    CASE
    WHEN COALESCE(tax_type_2_max_rate, tax_type_2_min_rate) = 0 THEN 'high'
    WHEN (taxBurdenRate_2 - COALESCE(tax_type_2_max_rate, tax_type_2_min_rate)) / COALESCE(tax_type_2_max_rate, tax_type_2_min_rate) < 0.1 THEN 'low'
    WHEN (taxBurdenRate_2 - COALESCE(tax_type_2_max_rate, tax_type_2_min_rate)) / COALESCE(tax_type_2_max_rate, tax_type_2_min_rate) <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    END AS risk_level_2,

    -- 税种3
    CASE
    WHEN tax_type_3_min_rate IS NULL OR taxBurdenRate_3 IS NULL OR taxBurdenRate_3 = 0 THEN 'no-risk'
    WHEN tax_type_3_min_rate = 0 AND COALESCE(tax_type_3_max_rate, 0) = 0 THEN 'no-risk'
    WHEN taxBurdenRate_3 >= tax_type_3_min_rate AND taxBurdenRate_3 <= COALESCE(tax_type_3_max_rate, tax_type_3_min_rate) THEN 'no-risk'
    WHEN taxBurdenRate_3 < tax_type_3_min_rate THEN
    CASE
    WHEN tax_type_3_min_rate = 0 THEN 'high'
    WHEN (tax_type_3_min_rate - taxBurdenRate_3) / tax_type_3_min_rate < 0.1 THEN 'low'
    WHEN (tax_type_3_min_rate - taxBurdenRate_3) / tax_type_3_min_rate <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    ELSE
    CASE
    WHEN COALESCE(tax_type_3_max_rate, tax_type_3_min_rate) = 0 THEN 'high'
    WHEN (taxBurdenRate_3 - COALESCE(tax_type_3_max_rate, tax_type_3_min_rate)) / COALESCE(tax_type_3_max_rate, tax_type_3_min_rate) < 0.1 THEN 'low'
    WHEN (taxBurdenRate_3 - COALESCE(tax_type_3_max_rate, tax_type_3_min_rate)) / COALESCE(tax_type_3_max_rate, tax_type_3_min_rate) <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    END AS risk_level_3,

    -- 税种5
    CASE
    WHEN tax_type_5_min_rate IS NULL OR taxBurdenRate_5 IS NULL OR taxBurdenRate_5 = 0 THEN 'no-risk'
    WHEN tax_type_5_min_rate = 0 AND COALESCE(tax_type_5_max_rate, 0) = 0 THEN 'no-risk'
    WHEN taxBurdenRate_5 >= tax_type_5_min_rate AND taxBurdenRate_5 <= COALESCE(tax_type_5_max_rate, tax_type_5_min_rate) THEN 'no-risk'
    WHEN taxBurdenRate_5 < tax_type_5_min_rate THEN
    CASE
    WHEN tax_type_5_min_rate = 0 THEN 'high'
    WHEN (tax_type_5_min_rate - taxBurdenRate_5) / tax_type_5_min_rate < 0.1 THEN 'low'
    WHEN (tax_type_5_min_rate - taxBurdenRate_5) / tax_type_5_min_rate <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    ELSE
    CASE
    WHEN COALESCE(tax_type_5_max_rate, tax_type_5_min_rate) = 0 THEN 'high'
    WHEN (taxBurdenRate_5 - COALESCE(tax_type_5_max_rate, tax_type_5_min_rate)) / COALESCE(tax_type_5_max_rate, tax_type_5_min_rate) < 0.1 THEN 'low'
    WHEN (taxBurdenRate_5 - COALESCE(tax_type_5_max_rate, tax_type_5_min_rate)) / COALESCE(tax_type_5_max_rate, tax_type_5_min_rate) <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    END AS risk_level_5,

    -- 税种6
    CASE
    WHEN tax_type_6_min_rate IS NULL OR taxBurdenRate_6 IS NULL OR taxBurdenRate_6 = 0 THEN 'no-risk'
    WHEN tax_type_6_min_rate = 0 AND COALESCE(tax_type_6_max_rate, 0) = 0 THEN 'no-risk'
    WHEN taxBurdenRate_6 >= tax_type_6_min_rate AND taxBurdenRate_6 <= COALESCE(tax_type_6_max_rate, tax_type_6_min_rate) THEN 'no-risk'
    WHEN taxBurdenRate_6 < tax_type_6_min_rate THEN
    CASE
    WHEN tax_type_6_min_rate = 0 THEN 'high'
    WHEN (tax_type_6_min_rate - taxBurdenRate_6) / tax_type_6_min_rate < 0.1 THEN 'low'
    WHEN (tax_type_6_min_rate - taxBurdenRate_6) / tax_type_6_min_rate <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    ELSE
    CASE
    WHEN COALESCE(tax_type_6_max_rate, tax_type_6_min_rate) = 0 THEN 'high'
    WHEN (taxBurdenRate_6 - COALESCE(tax_type_6_max_rate, tax_type_6_min_rate)) / COALESCE(tax_type_6_max_rate, tax_type_6_min_rate) < 0.1 THEN 'low'
    WHEN (taxBurdenRate_6 - COALESCE(tax_type_6_max_rate, tax_type_6_min_rate)) / COALESCE(tax_type_6_max_rate, tax_type_6_min_rate) <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    END AS risk_level_6,

    -- 税种7
    CASE
    WHEN tax_type_7_min_rate IS NULL OR taxBurdenRate_7 IS NULL OR taxBurdenRate_7 = 0 THEN 'no-risk'
    WHEN tax_type_7_min_rate = 0 AND COALESCE(tax_type_7_max_rate, 0) = 0 THEN 'no-risk'
    WHEN taxBurdenRate_7 >= tax_type_7_min_rate AND taxBurdenRate_7 <= COALESCE(tax_type_7_max_rate, tax_type_7_min_rate) THEN 'no-risk'
    WHEN taxBurdenRate_7 < tax_type_7_min_rate THEN
    CASE
    WHEN tax_type_7_min_rate = 0 THEN 'high'
    WHEN (tax_type_7_min_rate - taxBurdenRate_7) / tax_type_7_min_rate < 0.1 THEN 'low'
    WHEN (tax_type_7_min_rate - taxBurdenRate_7) / tax_type_7_min_rate <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    ELSE
    CASE
    WHEN COALESCE(tax_type_7_max_rate, tax_type_7_min_rate) = 0 THEN 'high'
    WHEN (taxBurdenRate_7 - COALESCE(tax_type_7_max_rate, tax_type_7_min_rate)) / COALESCE(tax_type_7_max_rate, tax_type_7_min_rate) < 0.1 THEN 'low'
    WHEN (taxBurdenRate_7 - COALESCE(tax_type_7_max_rate, tax_type_7_min_rate)) / COALESCE(tax_type_7_max_rate, tax_type_7_min_rate) <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    END AS risk_level_7,

    -- 税种9
    CASE
    WHEN tax_type_9_min_rate IS NULL OR taxBurdenRate_9 IS NULL OR taxBurdenRate_9 = 0 THEN 'no-risk'
    WHEN tax_type_9_min_rate = 0 AND COALESCE(tax_type_9_max_rate, 0) = 0 THEN 'no-risk'
    WHEN taxBurdenRate_9 >= tax_type_9_min_rate AND taxBurdenRate_9 <= COALESCE(tax_type_9_max_rate, tax_type_9_min_rate) THEN 'no-risk'
    WHEN taxBurdenRate_9 < tax_type_9_min_rate THEN
    CASE
    WHEN tax_type_9_min_rate = 0 THEN 'high'
    WHEN (tax_type_9_min_rate - taxBurdenRate_9) / tax_type_9_min_rate < 0.1 THEN 'low'
    WHEN (tax_type_9_min_rate - taxBurdenRate_9) / tax_type_9_min_rate <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    ELSE
    CASE
    WHEN COALESCE(tax_type_9_max_rate, tax_type_9_min_rate) = 0 THEN 'high'
    WHEN (taxBurdenRate_9 - COALESCE(tax_type_9_max_rate, tax_type_9_min_rate)) / COALESCE(tax_type_9_max_rate, tax_type_9_min_rate) < 0.1 THEN 'low'
    WHEN (taxBurdenRate_9 - COALESCE(tax_type_9_max_rate, tax_type_9_min_rate)) / COALESCE(tax_type_9_max_rate, tax_type_9_min_rate) <= 0.2  THEN 'medium'
    ELSE 'high'
    END
    END AS risk_level_9

    
    FROM (

    SELECT
  		 *,
CASE
            WHEN (@income_m := CASE
                WHEN settingValue IS NULL OR settingValue = '' THEN incomeMonthAmount125
                WHEN settingValue = 'FI_STANDARDS_EXECUTED' THEN incomeMonthAmount245
                WHEN settingValue = 'FI_STANDARDS_UNEXECUTED' THEN incomeMonthAmount116
                ELSE 0
            END) = 0 THEN NULL
            ELSE tax_type_1_amount / @income_m
        END AS taxBurdenRate_1,

        CASE
            WHEN @income_m = 0 THEN NULL
            ELSE tax_type_2_amount / @income_m
        END AS taxBurdenRate_2,
   
    CASE
            WHEN @income_m = 0 THEN NULL
            ELSE tax_type_3_amount / @income_m
        END AS taxBurdenRate_3,

        CASE
            WHEN @income_m = 0 THEN NULL
            ELSE tax_type_5_amount / @income_m
        END AS taxBurdenRate_5,

        CASE
            WHEN @income_m = 0 THEN NULL
            ELSE tax_type_6_amount / @income_m
        END AS taxBurdenRate_6,

        CASE
            WHEN @income_m = 0 THEN NULL
            ELSE tax_type_7_amount / @income_m
        END AS taxBurdenRate_7,

        CASE
            WHEN @income_m = 0 THEN NULL
            ELSE tax_type_9_amount / @income_m
        END AS taxBurdenRate_9,

        -- 年度税负率计算
        CASE
            WHEN (@income_y := CASE
                WHEN settingValue IS NULL OR settingValue = '' THEN incomeYearAmount125
                WHEN settingValue = 'FI_STANDARDS_EXECUTED' THEN incomeYearAmount245
                WHEN settingValue = 'FI_STANDARDS_UNEXECUTED' THEN incomeYearAmount116
                ELSE 0
            END) = 0 THEN NULL
            ELSE tax_type_1_year_amount / @income_y
        END AS taxBurdenRate_1_year,

        CASE
            WHEN @income_y = 0 THEN NULL
            ELSE tax_type_2_year_amount / @income_y
        END AS taxBurdenRate_2_year,

        CASE
            WHEN @income_y = 0 THEN NULL
            ELSE tax_type_3_year_amount / @income_y
        END AS taxBurdenRate_3_year,

        CASE
            WHEN @income_y = 0 THEN NULL
            ELSE tax_type_5_year_amount / @income_y
        END AS taxBurdenRate_5_year,

        CASE
            WHEN @income_y = 0 THEN NULL
            ELSE tax_type_6_year_amount / @income_y
        END AS taxBurdenRate_6_year,

        CASE
            WHEN @income_y = 0 THEN NULL
            ELSE tax_type_7_year_amount / @income_y
        END AS taxBurdenRate_7_year,

        CASE
            WHEN @income_y = 0 THEN NULL
            ELSE tax_type_9_year_amount / @income_y
        END AS taxBurdenRate_9_year
    ,COUNT(*) OVER() as total_count

    FROM (

    SELECT
    ec.id as easyacctgCustId,
    ec.assoc_tenant_id as assocTenantId,
    ec.cust_name as custName,
    ec.tax_no as taxNo,
    ec.taxiation_area as taxiationArea,
    ec.taxpayer_type_enum as taxpayerType,
    ec.tax_Industry_Id as taxIndustryId,



    (select cal_time from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 1) AS cal_time1,
    (select cal_time from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 2) AS cal_time2,
    (select cal_time from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 3) AS cal_time3,
    (select cal_time from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 5) AS cal_time5,
    (select cal_time from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 6) AS cal_time6,
    (select cal_time from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 7) AS cal_time7,
    (select cal_time from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 9) AS cal_time9,

    (
    SELECT tbt.min_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 1
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    -- 先找精准匹配
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR
    -- 如果精准匹配没找到，找默认配置
    (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY

    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    ) AS tax_type_1_min_rate,
    (
    SELECT tbt.max_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 1
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    )  AS tax_type_1_max_rate,
    (
    SELECT tbt.min_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 2
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    -- 先找精准匹配
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR
    -- 如果精准匹配没找到，找默认配置
    (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    -- 排序很重要：精准匹配的优先级高于默认配置
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    ) AS tax_type_2_min_rate,
    (
    SELECT tbt.max_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 2
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    )  AS tax_type_2_max_rate,
    (
    SELECT tbt.min_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 3
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    -- 先找精准匹配
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR
    -- 如果精准匹配没找到，找默认配置
    (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    -- 排序很重要：精准匹配的优先级高于默认配置
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    ) AS tax_type_3_min_rate,
    (
    SELECT tbt.max_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 3
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    )  AS tax_type_3_max_rate,
    (
    SELECT tbt.min_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 5
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    -- 先找精准匹配
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR
    -- 如果精准匹配没找到，找默认配置
    (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    -- 排序很重要：精准匹配的优先级高于默认配置
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    ) AS tax_type_5_min_rate,
    (
    SELECT tbt.max_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 5
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    )  AS tax_type_5_max_rate,

    (
    SELECT tbt.min_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 6
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    -- 先找精准匹配
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR
    -- 如果精准匹配没找到，找默认配置
    (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    -- 排序很重要：精准匹配的优先级高于默认配置
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    ) AS tax_type_6_min_rate,
    (
    SELECT tbt.max_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 6
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    )  AS tax_type_6_max_rate,
    (
    SELECT tbt.min_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 7
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    -- 先找精准匹配
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR
    -- 如果精准匹配没找到，找默认配置
    (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    -- 排序很重要：精准匹配的优先级高于默认配置
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    ) AS tax_type_7_min_rate,
    (
    SELECT tbt.max_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 7
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    )  AS tax_type_7_max_rate,

    (
    SELECT tbt.min_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 9
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    -- 先找精准匹配
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR
    -- 如果精准匹配没找到，找默认配置
    (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    -- 排序很重要：精准匹配的优先级高于默认配置
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    ) AS tax_type_9_min_rate,
    (
    SELECT tbt.max_tax_burden_rate
    FROM tax_burden_threshold tbt
    WHERE tbt.tenant_id = 482856029650944
    AND tbt.tax_type_id = 9
    AND tbt.taxpayer_type_enum = ec.taxpayer_type_enum
    AND (
    (tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area)
    OR (tbt.tax_industry_ld = 0 AND tbt.area_code = 'default')
    )
    ORDER BY
    CASE
    WHEN tbt.tax_industry_ld = ec.tax_Industry_Id AND tbt.area_code = ec.taxiation_area THEN 1
    ELSE 2
    END
    LIMIT 1
    )  AS tax_type_9_max_rate,

    (select tax_neg_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 1) as tax_type_1_amount,
    (select tax_neg_year_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 1) as tax_type_1_year_amount,
    (select tax_neg_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 2) as tax_type_2_amount,
    (select tax_neg_year_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 2) as tax_type_2_year_amount,
    (select tax_neg_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 3) as tax_type_3_amount,
    (select tax_neg_year_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 3) as tax_type_3_year_amount,
    (select tax_neg_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 5) as tax_type_5_amount,
    (select tax_neg_year_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 5) as tax_type_5_year_amount,
    (select tax_neg_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 6) as tax_type_6_amount,
    (select tax_neg_year_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 6) as tax_type_6_year_amount,
    (select tax_neg_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 7) as tax_type_7_amount,
    (select tax_neg_year_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 7) as tax_type_7_year_amount,
    (select tax_neg_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 9) as tax_type_9_amount,
    (select tax_neg_year_amount from fin_rpt as fr where fr.tenant_id = ec.assoc_tenant_id and fr.acctg_period = '202508' and fr.tax_type_id = 9) as tax_type_9_year_amount,

    (select sv.value from setting_value sv where sv.setting_type_id = 101069 and sv.tenant_id = ec.assoc_tenant_id) as settingValue,
    IFNULL(CAST(JSON_UNQUOTE(JSON_EXTRACT(tfrd_125.data, '$."#LRY110R006C003".value')) AS DECIMAL(20, 2)), 0) as incomeMonthAmount125,
    IFNULL(CAST(JSON_UNQUOTE(JSON_EXTRACT(tfrd_245.data, '$."#LRY020R006C003".value')) AS DECIMAL(20, 2)), 0)as incomeMonthAmount245,
    IFNULL(CAST(JSON_UNQUOTE(JSON_EXTRACT(tfrd_116.data, '$."#LRY001R006C003".value')) AS DECIMAL(20, 2)), 0)as incomeMonthAmount116,

    IFNULL(CAST(JSON_UNQUOTE(JSON_EXTRACT(tfrd_125.data, '$."#LRY110R006C004".value')) AS DECIMAL(20, 2)), 0) as incomeYearAmount125,
    IFNULL(CAST(JSON_UNQUOTE(JSON_EXTRACT(tfrd_245.data, '$."#LRY020R006C004".value')) AS DECIMAL(20, 2)), 0) as incomeYearAmount245,
    IFNULL(CAST(JSON_UNQUOTE(JSON_EXTRACT(tfrd_116.data, '$."#LRY001R006C004".value')) AS DECIMAL(20, 2)), 0) as incomeYearAmount116


     FROM easyacctg_cust ec
     

    LEFT JOIN tax_fin_rpt AS tfr
    ON tfr.tenant_id = ec.assoc_tenant_id
    AND tfr.acctg_period = '202508'
    AND tfr.tax_fin_rpt_def_id IN (41,42)

    LEFT JOIN customized_fin_rpt_def_detail cfrd_125
    ON cfrd_125.tenant_id = ec.assoc_tenant_id
    AND cfrd_125.tax_fin_rpt_def_id = tfr.tax_fin_rpt_def_id
    AND cfrd_125.tax_fin_rpt_def_detail_id = 125
    AND cfrd_125.is_template = 1
    LEFT JOIN tax_fin_rpt_detail tfrd_125
    ON tfrd_125.tenant_id = ec.assoc_tenant_id
    AND tfrd_125.tax_fin_rpt_id = tfr.id
    AND tfrd_125.tax_fin_rpt_def_detail_id = cfrd_125.id

    LEFT JOIN customized_fin_rpt_def_detail cfrd_116
    ON cfrd_116.tenant_id = ec.assoc_tenant_id
    AND cfrd_116.tax_fin_rpt_def_id = tfr.tax_fin_rpt_def_id
    AND cfrd_116.tax_fin_rpt_def_detail_id = 116
    AND cfrd_116.is_template = 1
    LEFT JOIN tax_fin_rpt_detail tfrd_116
    ON tfrd_116.tenant_id = ec.assoc_tenant_id
    AND tfrd_116.tax_fin_rpt_id = tfr.id
    AND tfrd_116.tax_fin_rpt_def_detail_id = cfrd_116.id

    LEFT JOIN customized_fin_rpt_def_detail cfrd_245
    ON cfrd_245.tenant_id = ec.assoc_tenant_id
    AND cfrd_245.tax_fin_rpt_def_id = tfr.tax_fin_rpt_def_id
    AND cfrd_245.tax_fin_rpt_def_detail_id = 245
    AND cfrd_245.is_template = 1
    LEFT JOIN tax_fin_rpt_detail tfrd_245
    ON tfrd_245.tenant_id = ec.assoc_tenant_id
    AND tfrd_245.tax_fin_rpt_id = tfr.id
    AND tfrd_245.tax_fin_rpt_def_detail_id = cfrd_245.id

    LEFT JOIN fin_rpt AS fr
    ON fr.tenant_id = ec.assoc_tenant_id
    AND fr.acctg_period = '202508'
    -- AND fr.tax_type_id = thtt.tax_type_id

    LEFT JOIN tenant t ON ec.assoc_tenant_id = t.id and t.is_deleted = 0

      LEFT JOIN easyacctg_declare_auth ceda ON ceda.tenant_id = ec.assoc_tenant_id
      AND ( ceda.external_system_enum = 'RPA' OR ceda.id IS NULL )
      AND ceda.auth_expired_date >= DATE_FORMAT(NOW(),'%Y-%m-%d')
  
    WHERE 
    EXISTS (
        SELECT 1 
        FROM tenant_hkj_version thv 
        WHERE thv.tenant_id = ec.assoc_tenant_id 
          AND thv.start_acctg_period <= '202508'
          AND thv.end_acctg_period >= '202508'
          AND EXISTS (
              SELECT 1 
              FROM tenant_hkj_tax_type thtt 
              WHERE thtt.tenant_id = ec.assoc_tenant_id 
                AND thtt.tenant_hkj_version_id = thv.id 
                AND thtt.tax_type_id IN (1,2,3,5,6,7,9)
          )
    )
    
    and ec.tenant_id = 482856029650944
    -- AND ec.taxpayer_type_enum = 'SMALL_TAXPAYER'
    AND ec.cust_status_enum = 'NORMAL'
    AND ec.status_enum = 'A'
    AND ec.assoc_tenant_id IS NOT NULL


   GROUP BY
   ec.id
    ORDER BY CASE WHEN ec.code IS NULL OR ec.code = '' THEN 1 ELSE 0 END,ec.code ASC,ec.id ASC
    ) AS base_data

      ) AS final_with_risk
    
    LIMIT 0, 500
