const SelectMonthOrDate = ({ exportPer, setExportPer }) => {
  return (
    <select
      className="select select-bordered w-full my-2"
      value={exportPer}
      onChange={(e) => setExportPer(e.target.value)}>
      <option value="" disabled>
        Select Export Per Date Or Month
      </option>
      <option value="month">Month</option>
      <option value="date">Date</option>
    </select>
  );
};

export default SelectMonthOrDate;
