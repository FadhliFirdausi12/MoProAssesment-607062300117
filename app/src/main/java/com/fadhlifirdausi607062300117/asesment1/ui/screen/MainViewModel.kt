package com.fadhlifirdausi607062300117.asesment1.ui.screen

import androidx.lifecycle.ViewModel
import com.fadhlifirdausi607062300117.asesment1.model.HelpLog

class MainViewModel:ViewModel() {
    val data = listOf(
        HelpLog(
            id = 1,
            judul = "Bagaimana cara mencatat aktivitas harian?",
            deskripsi = "Untuk mencatat aktivitas harian, buka halaman utama lalu tekan tombol '+' di pojok kanan bawah. Pilih jenis aktivitas, isi detail seperti waktu dan durasi, lalu simpan."
        ),
        HelpLog(
            id = 2,
            judul = "Apa fungsi dari fitur rekomendasi?",
            deskripsi = "Fitur rekomendasi akan menampilkan pelatihan atau pekerjaan yang sesuai dengan aktivitas atau minat yang kamu masukkan. Ini membantumu berkembang secara personal dan profesional."
        ),
        HelpLog(
            id = 3,
            judul = "Bagaimana cara mengubah warna tema aplikasi?",
            deskripsi = "Masuk ke menu Pengaturan (ikon roda gigi di pojok kanan atas), lalu pilih Tema Warna. Kamu bisa memilih antara hijau, biru, atau oranye sesuai preferensimu."
        ),
        HelpLog(
            id = 4,
            judul = "Kenapa data saya tidak tersimpan?",
            deskripsi = "Pastikan kamu memiliki koneksi internet saat menyimpan data. Jika kamu sedang offline, data akan disimpan sementara dan diunggah otomatis saat koneksi kembali tersedia."
        ),
        HelpLog(
            id = 5,
            judul = "Apakah aplikasi ini gratis?",
            deskripsi = "Ya! WellVibe sepenuhnya gratis untuk digunakan. Beberapa fitur premium akan tersedia di masa depan, tapi semua fitur utama tetap gratis tanpa batasan."
        ),
        HelpLog(
            id = 6,
            judul = "Bagaimana menjaga privasi saya?",
            deskripsi = "Kami menghargai privasimu. Semua data pengguna dienkripsi dan tidak dibagikan ke pihak ketiga. Kamu bisa membaca kebijakan privasi lengkap kami di halaman Tentang Aplikasi."
        ),
        HelpLog(
            id = 7,
            judul = "Bisakah saya mengganti nama pengguna?",
            deskripsi = "Saat ini, nama pengguna ditentukan saat registrasi. Kami sedang mengembangkan fitur untuk mengubah nama pengguna dalam versi berikutnya. Nantikan update kami!"
        ),
        HelpLog(
            id = 8,
            judul = "Apa itu fitur 'Wellness Tracker'?",
            deskripsi = "Fitur ini membantu kamu melacak mood, pola tidur, dan tingkat energi setiap harinya. Cocok untuk menjaga kesehatan mental dan fisik secara seimbang."
        ),
        HelpLog(
            id = 9,
            judul = "Bagaimana cara melaporkan bug?",
            deskripsi = "Jika menemukan bug atau error, kamu bisa melaporkannya lewat menu Bantuan di Pengaturan. Sertakan detail kejadian dan screenshot jika memungkinkan agar kami bisa segera memperbaikinya."
        ),
        HelpLog(
            id = 10,
            judul = "Apakah saya bisa backup data saya?",
            deskripsi = "Ya, data kamu otomatis disinkronkan dengan akun Google saat login. Namun, kamu juga bisa melakukan backup manual di menu Pengaturan > Backup & Restore."
        )
    )
}