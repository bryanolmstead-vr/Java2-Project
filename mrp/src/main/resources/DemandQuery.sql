WITH RECURSIVE demand_cte AS ( 
    SELECT                                                                     
        b.sku AS child_sku,                                                   
        b.parent_sku,                                                          
        CASE                                                                   
            WHEN COALESCE(p.stock, 0) >= :demand_qty THEN 0                   
            ELSE (b.quantity * (:demand_qty - COALESCE(p.stock, 0)))           
        END AS required_qty                                                    
    FROM bom AS b                                                               
    LEFT JOIN part AS p ON b.parent_sku = p.sku                                 
    WHERE b.parent_sku = :demand_sku                                            
    UNION ALL

    SELECT
        b.sku AS child_sku,
        b.parent_sku,
        CASE 
            WHEN COALESCE(parent_p.stock, 0) >= d.required_qty THEN 0
            ELSE ((d.required_qty - COALESCE(parent_p.stock, 0)) * b.quantity)
        END AS required_qty 
    FROM bom AS b
    JOIN demand_cte AS d ON b.parent_sku = d.child_sku
    LEFT JOIN part AS parent_p ON b.parent_sku = parent_p.sku 
    LEFT JOIN part AS child_p ON b.sku = child_p.sku 
    WHERE (CASE 
               WHEN COALESCE(parent_p.stock, 0) >= d.required_qty THEN 0
               ELSE ((d.required_qty - COALESCE(parent_p.stock, 0)) * b.quantity)
           END) > 0 
),
raw_materials AS (
    SELECT p.sku AS raw_material_sku, p.description AS raw_material_description
    FROM part AS p
    LEFT JOIN bom AS b ON p.sku = b.parent_sku
    WHERE b.parent_sku IS NULL
)

SELECT 
    r.raw_material_sku AS raw_material_sku,
    SUM(COALESCE(d.required_qty, 0)) AS total_raw_demand, 
    COALESCE(p.stock, 0) AS stock_available, 
    (SUM(COALESCE(d.required_qty, 0)) - COALESCE(p.stock, 0)) AS total_required_qty,
    r.raw_material_description AS raw_material_description 
FROM raw_materials AS r
LEFT JOIN demand_cte AS d ON r.raw_material_sku = d.child_sku
LEFT JOIN part AS p ON r.raw_material_sku = p.sku
GROUP BY r.raw_material_sku
HAVING total_required_qty > 0; 
