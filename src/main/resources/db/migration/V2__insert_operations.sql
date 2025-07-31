-- Insert initial operation types and their respective costs

INSERT INTO operations (type, cost) VALUES
('ADDITION',       0.01),
('SUBTRACTION',    0.01),
('MULTIPLICATION', 0.02),
('DIVISION',       0.02),
('SQUARE_ROOT',    0.03),
('RANDOM_STRING',  0.05)
ON CONFLICT (type) DO NOTHING;
