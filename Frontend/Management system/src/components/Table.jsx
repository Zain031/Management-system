const Table = ({
  arrayData = [],
  page = 1,
  tableHeader = [],
  notFoundMessage = "No data found",
  customRender = {}, // Tambahkan properti customRender untuk styling custom.
  excludeColumns = [], // Tambahkan properti excludeColumns untuk pengecualian render.
  renderActions = () => {},
}) => {
  const renderTableHeader = () =>
    tableHeader
      .filter((item) => !excludeColumns.includes(item.charAt(0).toUpperCase() + item.slice(1).toLowerCase())) // Filter header berdasarkan excludeColumns
      .map((item, index) => <th key={index}>{item}</th>);

  const renderTableRow = (item, index) => {
    // Tentukan warna berdasarkan status
    const statusColor =
      item.status === "Pending"
        ? "bg-blue-600 w-14"
        : item.status === "Completed"
        ? "bg-green-600 w-14"
        : item.status === "Canceled"
        ? "bg-red-600 w-14"
        : "";

    return (
      <tr key={index}>
        <th>{(page - 1) * 10 + (index + 1)}</th>
        {Object.entries(item)
          .filter(([key]) => !excludeColumns.includes(key))
          .map(([key, value]) => (
            <td key={key}>
              {key === "status" ? (
                <span className={`px-2 py-1 rounded-md text-base text-white ${statusColor}`}>
                  {value}
                </span>
              ) : (
                value
              )}
            </td>
          ))}
        {renderActions(item)}
      </tr>
    );
  };

  return (
    <div className="overflow-x-auto shadow-lg outline outline-1 outline-slate-300 rounded-md mt-2">
      <table className="table">
        <thead>
          <tr>
            <th></th>
            {renderTableHeader()}
          </tr>
        </thead>
        <tbody>
          {Array.isArray(arrayData) && arrayData.length > 0 ? (
            arrayData.map(renderTableRow)
          ) : (
            <tr>
              <td colSpan={tableHeader.length + 1} className="text-center">
                {notFoundMessage}
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default Table;
