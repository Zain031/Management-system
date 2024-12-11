import yearOptionRange from "../../utils/yearOptionRange";

const SelectYear = ({ selectedYear, setSelectedYear }) => {
  return (
    <select
      className="select select-bordered w-full my-2"
      defaultValue={selectedYear}
      placeholder="Select Year"
      onChange={(e) => setSelectedYear(e.target.value)}>
      <option value="" disabled>
        Select Year
      </option>
      {yearOptionRange().map((year) => (
        <option key={year} value={year}>
          {year}
        </option>
      ))}
    </select>
  );
};

export default SelectYear;
