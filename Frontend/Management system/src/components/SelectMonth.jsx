const SelectMonth = ({ selectedMonth, setSelectedMonth }) => {
  return (
    <select
      className="select select-bordered w-full my-2"
      defaultValue={selectedMonth}
      placeholder="Select Month"
      onChange={(e) => setSelectedMonth(e.target.value)}>
      <option value="" disabled>
        Select Month
      </option>
      Select Month
      <option value="january">January</option>
      <option value="february">February</option>
      <option value="march">March</option>
      <option value="april">April</option>
      <option value="may">May</option>
      <option value="june">June</option>
      <option value="july">July</option>
      <option value="august">August</option>
      <option value="september">September</option>
      <option value="october">October</option>
      <option value="november">November</option>
      <option value="december">December</option>
    </select>
  );
};

export default SelectMonth;
