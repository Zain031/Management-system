import Table from "../Table";

const ShowDetailOrderModal = ({
  selectedOrderForShowingDetail,
  onShowDetailClose,
}) => {
  return (
    <dialog id="show_detail_modal" className="modal">
      <div className="modal-box">
        <h3 className="font-bold text-3xl">Order</h3>
        <section className="overflow-x-auto text-black">
          <Table
            arrayData={[selectedOrderForShowingDetail]}
            tableHeader={[
              "Id Order",
              "Customer Name",
              "Order Date",
              "Status",
              "Total",
            ]}
            excludeColumns={["details"]}
            customRender={{
              orderDate: (value) => (
                <span className="tooltip w-32 text-start" data-tip="Order Date">
                  {value}
                </span>
              ),
            }}
          />
        </section>
        <section className="overflow-x-auto">
          <h3 className="font-bold text-lg">Order details:</h3>
          <Table
            arrayData={selectedOrderForShowingDetail?.details}
            tableHeader={["Product Name", "Quantity", "Price"]}
            excludeColumns={["id"]}
          />
        </section>
        <div className="modal-action">
          <form method="dialog">
            <button className="btn" onClick={onShowDetailClose}>
              Close
            </button>
          </form>
        </div>
      </div>
    </dialog>
  );
};

export default ShowDetailOrderModal;
