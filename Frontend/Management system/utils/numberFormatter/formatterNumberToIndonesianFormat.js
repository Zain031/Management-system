export const formatterNumberToIndonesianFormat = (value) => {
  return new Intl.NumberFormat("id-ID").format(value);
};
