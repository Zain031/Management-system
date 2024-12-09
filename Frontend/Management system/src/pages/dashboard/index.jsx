import { useDispatch, useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { Select, SelectItem } from "@nextui-org/select";
import { Card } from "@nextui-org/card";
import { fetchDashboardData } from "../../redux/feature/dashboardSlice";
import DynamicGroupBarChart from "../../components/DynamicGroupBarChart";
import { numberToIDR } from "../../../utils/numberFormatter/numberToIDR";

const AdminDashboard = () => {
  const dispatch = useDispatch();
  const {
    monthlyPerformance,
    totalProfit,
    year,
    totalOrder,
    totalPriceOrder,
    totalPriceInventory,
    loading,
  } = useSelector((state) => state.dashboard);

  const [filterYear, setFilterYear] = useState(new Date().getFullYear());

  useEffect(() => {
    dispatch(fetchDashboardData(2024));
  }, []);
  useEffect(() => {
    dispatch(fetchDashboardData(filterYear));
  }, [filterYear]);

  return (
    <section className="flex flex-col gap-6">
      <section className="absolute top-2 right-10 flex gap-6 w-1/4">
        <Select
          label="Year"
          selectedKeys={[String(filterYear)]}
          variant="bordered"
          onChange={(e) => setFilterYear(e.target.value)}>
          {Array.from({ length: 5 }, (_, index) => (
            <SelectItem key={String(new Date().getFullYear() - index)}>
              {String(new Date().getFullYear() - index)}
            </SelectItem>
          ))}
        </Select>
      </section>
      <h2 className="text-4xl text-mainSoil text-center mb-6 font-extrabold">
        Admin Dashboard
      </h2>
      <section className="flex gap-6 justify-center items-center mb-12">
        <Card className="w-[24%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-[#82ca9d] flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Income
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-3xl">
                {new Intl.NumberFormat("id-ID", {
                  style: "currency",
                  currency: "IDR",
                }).format(totalPriceOrder)}
              </p>
            </section>
          </section>
        </Card>
        <Card className="w-[24%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-[#8884d8] flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Profit ({filterYear})
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-3xl">
                Rp {totalProfit.toLocaleString("id-ID")}
              </p>
            </section>
          </section>
        </Card>
        <Card className="w-[24%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-[#FF8000] flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Order ({filterYear})
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-3xl">
                {totalOrder.toLocaleString("id-ID")} Order
              </p>
            </section>
          </section>
        </Card>
        <Card className="w-[24%] min-h-[200px]" isPressable isHoverable>
          <section className="w-full h-[200px] bg-[#FA4032] flex justify-center items-center flex-col">
            <section>
              <h3 className="text-start font-semibold text-mainSoil">
                Total Purchase ({filterYear})
              </h3>
              <p className="text-start font-extrabold text-mainGreen text-3xl">
                {totalPriceInventory.toLocaleString("id-ID", {
                  style: "currency",
                  currency: "IDR",
                })}
              </p>
            </section>
          </section>
        </Card>
      </section>
      <section className="flex gap-4">
        {/* <section className="bg-neutral-50 w-full rounded-xl py-4 px-6">
          <ChartCompose
            chartTitle={"Monthly Performance"}
            data={monthlyPerformance}
            barDataKey={"profit"}
            barName={"Profit"}
            areaDataKey={"totalPriceInventory"}
            areaName={"Inventory"}
            formatter={numberToIDR}
            lineDataKey={"totalPriceOrder"}
            lineName={"Revenue"}
            XAxisDataKey={"month"}
          />
        </section> */}
        {/* <MixBarChart
          data={monthlyPerformance}
          chartTitle="Monthly Performance"
          XAxisDataKey="month"
          formatter={numberToIDR}
          barGroupOne={true}
          barGroupTwo={true}
          barGroupOneBars={[
            { dataKey: "profit", name: "Profit", fill: "#8884d8" },
            { dataKey: "totalPriceOrder", name: "Revenue", fill: "#82ca9d" },
          ]}
          barGroupTwoBars={[
            {
              dataKey: "totalPriceInventory",
              name: "Inventory",
              fill: "#82ca9d",
            },
            {
              dataKey: "totalOrder",
              name: "Total order",
              fill: "#ffc658",
            },
          ]}
        /> */}

        <DynamicGroupBarChart
          data={monthlyPerformance}
          chartTitle="Monthly Performance"
          XAxisDataKey="month"
          formatter={numberToIDR}
          bars={[
            {
              dataKey: "profit",
              name: "Profit",
              fill: "#8884d8",
              stackId: "group1",
            },
            {
              dataKey: "totalPriceOrder",
              name: "Income",
              fill: "#82ca9d",
              stackId: "group1",
            },
            {
              dataKey: "totalPriceInventory",
              name: "Purchase",
              fill: "#FA4032",
              stackId: "group2",
            },
          ]}
        />
      </section>
    </section>
  );
};

export default AdminDashboard;
