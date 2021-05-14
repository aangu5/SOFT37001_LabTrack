-- Hazard Statement
INSERT INTO hazard_statement VALUES ('90255c94-b3fe-466e-a219-7ece56c7f965', 'H225:Highly flammable liquid and vapour', 'state');

-- Pictograms
INSERT INTO pictogram VALUES ('170b014a-0483-4e1f-9e90-0f82ddde1629', 'Flammable', 'GHS02');
INSERT INTO pictogram VALUES ('336d09ed-4a40-4cc6-abb8-c636ae483525', 'Explosive', 'GHS01');
INSERT INTO pictogram VALUES ('8ee1fd0d-879b-4d25-b8d5-44fe01994ce6', 'Oxidising', 'GHS03');

INSERT INTO chemical_hazard_card VALUES ('df53353f-62c6-4821-b391-3d7056b38c69', 'Ethanol', '2021-05-09 14:23:47.000000', true, '64-17-5', 'DANGER', '83ac436c-1029-4cfd-823f-35cbc6b186c3');
INSERT INTO chemical_hazard_card_hazard_statements VALUES ('df53353f-62c6-4821-b391-3d7056b38c69', '90255c94-b3fe-466e-a219-7ece56c7f965');