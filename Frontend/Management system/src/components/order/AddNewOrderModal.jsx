import { numberToIDR } from "../../../utils/numberFormatter/numberToIDR";
import ButtonAdd from "../buttons/ButtonAdd";
import { Trash2 } from "lucide-react";

const AddNewOrderModal = ({
  handleAddNewOrder,
  setCustomerNameForCreatingOrder,
  customerNameForCreatingOrder,
  products,
  idProductForCreatingOrder,
  setIdProductForCreatingOrder,
  quantityForCreatingOrder,
  setQuantityForCreatingOrder,
  addProductForOrder,
  cart,
  isSuccessAddOrder,
  redirectURL,
  onButtonCloseAddNewOrder,
}) => {
  return (
    <dialog id="add_new_order_modal" className="modal">
      <div className="modal-box">
        <form onSubmit={handleAddNewOrder}>
          <input
            type="name"
            name="customerName"
            className="input input-bordered input-ghost w-full my-2"
            placeholder="Customer Name"
            value={customerNameForCreatingOrder}
            onChange={(e) => setCustomerNameForCreatingOrder(e.target.value)}
          />
          <section className="flex gap-2 justify-center items-center">
            <select
              name="customer_name"
              value={idProductForCreatingOrder}
              onChange={(e) => setIdProductForCreatingOrder(e.target.value)}
              className="select select-bordered w-full">
              <option selected value="">
                Select Product Name
              </option>
              {products.map((product) => (
                <option key={product.id_product} value={product.id_product}>
                  {product.product_name}
                </option>
              ))}
            </select>
            <input
              type="number"
              name="quantity"
              value={quantityForCreatingOrder}
              onChange={(e) => setQuantityForCreatingOrder(e.target.value)}
              className="input input-bordered input-ghost w-full my-2"
              placeholder="Quantity"
            />
            <ButtonAdd onPress={addProductForOrder} dataTip={"Product"} />
          </section>
          <section>
            {cart?.orderDetails.map((detail) => (
              <div
                key={detail.id_order_details}
                className="flex gap-2 justify-center items-center">
                <span>{detail.product_name}</span>
                <span>{detail.quantity}</span>
                <button
                  type="button"
                  className="tooltip"
                  data-tip="Remove Product">
                  <Trash2 size={50} color="red" />
                </button>
              </div>
            ))}
            {cart.totalPrice > 0 && (
              <div className="flex gap-2 justify-center items-center">
                <span>Total:</span>
                <span>{numberToIDR(cart.totalPrice)}</span>
              </div>
            )}
          </section>

          {isSuccessAddOrder ? (
            <button
              className="btn btn-outline btn-primary w-full"
              type="button"
              onClick={() => {
                if (redirectURL) {
                  window.location.href = redirectURL;
                } else {
                  alert("Redirect URL tidak ditemukan!");
                }
              }}>
              PAY
            </button>
          ) : (
            <button
              className="btn btn-outline btn-primary w-full"
              type="submit">
              Submit
            </button>
          )}
        </form>
        <div className="modal-action">
          <form method="dialog">
            <button className="btn" onClick={onButtonCloseAddNewOrder}>
              Close
            </button>
          </form>
        </div>
      </div>
    </dialog>
  );
};

export default AddNewOrderModal;
