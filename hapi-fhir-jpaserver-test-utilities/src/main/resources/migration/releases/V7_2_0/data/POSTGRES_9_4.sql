INSERT INTO TRM_CONCEPT_MAP_GRP_ELM_TGT (PID, TARGET_CODE, CONCEPT_MAP_URL, TARGET_DISPLAY, TARGET_EQUIVALENCE, SYSTEM_URL, SYSTEM_VERSION, VALUESET_URL, CONCEPT_MAP_GRP_ELM_PID) VALUES (61, NULL, NULL, 'PYRIDOXINE', 'UNMATCHED', NULL, NULL, NULL, 60);
INSERT INTO HFJ_BINARY_STORAGE (CONTENT_ID, RESOURCE_ID, CONTENT_TYPE, STORAGE_CONTENT_BIN, PUBLISHED_DATE ) VALUES ('1', '2', 'TEXT', '\x48656c6c6f20776f726c6421', '2023-06-15 09:58:42.92');
INSERT INTO TRM_CONCEPT (PID, CODEVAL, PARENT_PIDS_VC ) VALUES (1, 'aCode', '1 2 3 4');
INSERT INTO TRM_CONCEPT_PROPERTY (PID, PROP_KEY, PROP_VAL_BIN, PROP_TYPE) VALUES (1, 'key', '\x48656c6c6f20776f726c6421', 1);
INSERT INTO TRM_VALUESET_CONCEPT (PID, VALUESET_PID, VALUESET_ORDER, SOURCE_DIRECT_PARENT_PIDS_VC, SYSTEM_URL, CODEVAL) VALUES (1, 59, 1, '1 2 3 4 5 6', 'http://systemUlr', 'codeVal');
