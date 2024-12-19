export default function formatDate(inputDate) {
  const monthNames = [
    "Januari",
    "Februari",
    "Maret",
    "April",
    "Mei",
    "Juni",
    "Juli",
    "Agustus",
    "September",
    "Oktober",
    "November",
    "Desember",
  ];

  const [year, month, day] = inputDate.split("-");
  return `${day} ${monthNames[parseInt(month, 10) - 1]} ${year}`;
}
