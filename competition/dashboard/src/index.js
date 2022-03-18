import RobotSvg from './robot-svg';
import intakeTopArm from './images/Orange.png';
import intakeSidePlate from "./images/intake_side_plate.png";
import { html, LitElement } from 'lit';


class RobotSvgElement extends LitElement {

  constructor() {
    super();
  }

  firstUpdated() {
    const robotContainer = this.renderRoot.querySelector('.robot-container');
    const robotSvg = new RobotSvg(robotContainer, {
      size: { width: 300, height: 220 },
      children: {
        intakeTopArm: {
          image: intakeTopArm,
          size: { width: 120, height: 100 },
          translate: { x: 41.5, y: 58 },
          transformOrigin: "112 82",
          children: {
            intakeSidePlate: {
              src: intakeSidePlate,
              size: { width: 100, height: 75 },
              translate: { x: 110, y: 169 },
              transformOrigin: "92 20"
            }
          }
        },
        plateOne: {
          image: 'src/images/intake_motor_mount.png',
          size: { width: 51, height: 100 },
          translate: { x: 64, y: 117 },
        },
        wheelOne: {
          image: 'src/images/wheel.png',
          size: { width: 67, height: 67 },
          translate: { x: 20, y: 151 },
          transformOrigin: '34px 34px',
        },
        wheelTwo: {
          image: 'src/images/wheel.png',
          size: { width: 67, height: 67 },
          translate: { x: 117, y: 151.5 },
          transformOrigin: '34px 34px',
        },
        wheelThree: {
          image: 'src/images/wheel.png',
          size: { width: 67, height: 67 },
          translate: { x: 214.5, y: 151 },
          transformOrigin: '34px 34px',
        },
        base: {
          image: 'src/images/robot_base.png',
          size: { width: 300, height: 200 },
        },
      }
    });

    let degrees = 0;

    setInterval(() => {
      degrees -= 5;
      robotSvg.rotatePart('wheelOne', degrees);
      robotSvg.rotatePart('wheelTwo', degrees);
      robotSvg.rotatePart('wheelThree', degrees);
    }, 20);
  }

  render() {
    return html`
      <div class="robot-container"></div>
    `;
  }
}

customElements.define('kwarqs-robot-drawing', RobotSvgElement);