-- Mengaktifkan ekstensi pgcrypto untuk bcrypt
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Menambahkan user baru hanya jika belum ada user dengan email tersebut
INSERT INTO users (email, name, password, role, created_at, updated_at)
VALUES (
           'Aditya07bayu@gmail.com',
           'Aditya Bayu Prabowo',
           crypt('Cilacap12.', gen_salt('bf')),
           'SUPER_ADMIN',
           NOW(),
           NOW()
       )
ON CONFLICT (email) DO NOTHING;  -- Tidak lakukan apa-apa jika email sudah ada
