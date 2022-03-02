// https://svgjs.dev/docs/3.0/getting-started/
import { SVG, G } from "@svgdotjs/svg.js";
import imagePath from "./oie_transparent.png";

const draw = SVG().addTo("body").size(500, 700);
const group = draw.group().attr({
  "transform-origin": "5 100",
  transform: "rotate(40)"
});
const redRect = draw.rect(10, 100).attr({ fill: "#f00" });
const blueRect = draw.rect(100, 10).attr({
  fill: "#00f",
  "transform-origin": "5 5",
  transform: "rotate(-25)"
});

group.add(redRect);
group.add(blueRect);

draw.image(imagePath).size(116, 75).translate(0, 100);
const upArm = draw
  .image("src/images/intake_top_arm.png")
  .size(120, 100)
  // .translate(189.5, 171.5)
  .attr({
    "transform-origin": "120 85",
    transform: "translate(189.5, 171.5) rotate(25)"
  });

const wheelOne = draw
  .image("src/images/wheel.png")
  .size(67, 67)
  .translate(233, 245)
  .attr({
    "transform-origin": "center"
  });
const wheelThree = draw
  .image("src/images/wheel.png")
  .size(67, 67)
  .translate(401.5, 245)
  .attr({
    "transform-origin": "center"
  });
const wheelTwo = draw
  .image("src/images/wheel.png")
  .size(67, 67)
  .translate(317, 251.5)
  .attr({
    "transform-origin": "center"
  });
const base = draw
  .image("src/images/robot_base.png")
  .size(300, 200)
  .translate(200, 100);

const plateOne = draw
  .image("src/images/intake_motor_mount.png")
  .size(51, 100)
  .translate(264, 217);

const robot = draw.group();
const driveBase = draw.group();

driveBase.add(plateOne);
driveBase.add(wheelOne);
driveBase.add(wheelTwo);
driveBase.add(wheelThree);
driveBase.add(base);
driveBase.attr({
  //transform: "rotate(20) translate(150, 10) scale(1)"
});
// var rect = draw.rect(100, 100).attr({ fill: "#f06" });
// SVG().G;

// SVG()
//   .addTo("body")
//   .size(160, 160)
//   .path()
//   .attr({
//     fill: "none",
//     stroke: "#000",
//     "stroke-width": 2
//   })
//   .M(10, 80)
//   .A(150, 75, 30, 0, 0, 150, 80);
