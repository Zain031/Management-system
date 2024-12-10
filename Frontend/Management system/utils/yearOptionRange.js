const yearOptionRange = () => {
  const currentYear = new Date().getFullYear();
  const endRangeYear = 2023;
  const yearRange = [];
  for (let i = currentYear; i >= endRangeYear; i--) {
    yearRange.push(i);
  }
  return yearRange;
};

export default yearOptionRange;
