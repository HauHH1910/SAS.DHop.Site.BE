UPDATE status
SET status_type = 'ACTIVE'
WHERE status_type IS NULL
   OR TRIM(status_type) = '';

UPDATE status
SET status_type = 'ACTIVE'
WHERE UPPER(TRIM(status_type)) = 'ACTIVE';

UPDATE status
SET status_type = 'INACTIVE'
WHERE UPPER(TRIM(status_type)) = 'INACTIVE';

UPDATE status
SET status_type = 'PENDING'
WHERE UPPER(TRIM(status_type)) = 'PENDING';

UPDATE status
SET status_type = 'ACTIVE'
WHERE UPPER(TRIM(status_type)) NOT IN ('ACTIVE', 'INACTIVE', 'PENDING');
