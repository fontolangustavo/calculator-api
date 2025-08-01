-- Insert initial operation types and their respective costs
MERGE INTO operations (type, cost) KEY(type) VALUES
  ('ADDITION',       0.01),
  ('SUBTRACTION',    0.01),
  ('MULTIPLICATION', 0.02),
  ('DIVISION',       0.02),
  ('SQUARE_ROOT',    0.03),
  ('RANDOM_STRING',  0.05);
