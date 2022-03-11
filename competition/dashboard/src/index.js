// https://svgjs.dev/docs/3.0/getting-started/
import { SVG, G } from "@svgdotjs/svg.js";
import imagePath from "./oie_transparent.png";
import intakeTopArm from './images/intake_top_arm.png';
import intakeSidePlate from "./images/intake_side_plate.png";

const draw = SVG().addTo("body").size(500, 700);

// const svgConfig = {
//   name: 'driveBase',
//   type: 'group',
//   children: [
//     {
//       name: 'plateOne',
//       type: 'image',
//       src: 'src/images/intake_motor_mount.png',
//       size: { x: 51, y: 100 },
//       transform: "translate(264 217)"
//     },
//     {
//       name: 'wheelOne',
//       type: 'image',
//       src: 'src/images/wheel.png',
//       size: { x: 233, y: 245 },
//       transformOrigin: 'center',
//       transform: "translate(233, 245)"
//     },
//   ]
// };



// const wheelOne = draw
//   .image("src/images/wheel.png")
//   .size(67, 67)
//   .translate(233, 245)
//   .attr({
//     "transform-origin": "center"
//   });


const upArm = draw
  .image(intakeTopArm)
  .size(120, 100)
  // .translate(189.5, 171.5)
  .attr({
    "transform-origin": "112 82",
    transform: "translate(189.5, 171.5) rotate(0)"
  });

  const intakeThing = draw 
    .image(intakeSidePlate)
    .size(100, 75)
    .attr({
      "transform-origin": "92 20",
      transform: "translate(110, 169) rotate(0)"
    });



  // const downArm = draw
  //   .image()

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
  .translate(200, 100)

const plateOne = draw
  .image("src/images/intake_motor_mount.png")
  .size(51, 100)
  .translate(264, 217);




const robot = draw.group();
const intake = draw.group();


const driveBase = draw.group();

driveBase.add(plateOne);
driveBase.add(wheelOne);
driveBase.add(wheelTwo);
driveBase.add(wheelThree);
driveBase.add(base);

intake.add(upArm);

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

export default {
    elements: {
        'img': {
            properties: {
                src: { type: 'String' },
            }
        }
    }
}