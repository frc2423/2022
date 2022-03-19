import RobotSvg from './robot-svg';
import intakeTopArm from './images/Orange.png';
import intakeSidePlate from "./images/intake_side_plate.png";
import plateOne from './images/intake_motor_mount.png';
import wheel from './images/wheel.png';
import base from './images/robot_base.png';
import blueCargo from './images/blue_cargo.png';
import redCargo from './images/red_cargo.png';
import { html, css, LitElement } from 'lit';


class RobotSvgElement extends LitElement {

  static styles = css`
    :host {
      display: block;
      width: 300px;
      height: 300px;
    }
  `;

  static properties = {
    robotArmSetpoint: { type: Number, attribute: 'robot-arm-setpoint', reflect: true },
    robotArmSetpointMax: { type: Number, attribute: 'robot-arm-setpoint-max', reflect: true },
    rotationsPerSecond: { type: Number, attribute: 'rotations-per-second', reflect: true },
    allianceColor: { type: String, attribute: 'allience-color', reflect: true },
    ballCount: { type: Number, attribute: 'ball-count', reflect: true }
  };

  constructor() {
    super();
    this.robotArmSetpoint = 0;
    this.robotArmSetpointMax = -10;
    this.robotSvg = null;
    this.rotationsPerSecond = 0;
    this.allianceColor = "RED";
    this.ballCount = 0;
  }

  firstUpdated() {
    const robotContainer = this.renderRoot.querySelector('.robot-container');
    const robotSvg = new RobotSvg(robotContainer, {
      size: { width: 300, height: 220 },
      children: {
        blueCargo1: {
          image: blueCargo,
          size: { width: 70, height: 70 },
          translate: { x: 180, y: 60 }
        },
        blueCargo2: {
          image: blueCargo,
          size: { width: 70, height: 70 },
          translate: { x: 120, y: 90 }
        },
        redCargo1: {
          image: redCargo,
          size: { width: 70, height: 70 },
          translate: { x: 180, y: 60 }
        },
        redCargo2: {
          image: redCargo,
          size: { width: 70, height: 70 },
          translate: { x: 120, y: 90 }
        },
        intakeTopArm: {
          image: intakeTopArm,
          size: { width: 120, height: 100 },
          translate: { x: 41.5, y: 58 },
          transformOrigin: "60 90",
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
          image: plateOne,
          size: { width: 51, height: 100 },
          translate: { x: 64, y: 117 },
        },
        wheelOne: {
          image: wheel,
          size: { width: 67, height: 67 },
          translate: { x: 20, y: 151 },
          transformOrigin: '34px 34px',
        },
        wheelTwo: {
          image: wheel,
          size: { width: 67, height: 67 },
          translate: { x: 117, y: 151.5 },
          transformOrigin: '34px 34px',
        },
        wheelThree: {
          image: wheel,
          size: { width: 67, height: 67 },
          translate: { x: 214.5, y: 151 },
          transformOrigin: '34px 34px',
        },
        base: {
          image: base,
          size: { width: 300, height: 200 },
        },
      }
    });

    let degrees = 0;

    setInterval(() => {
      degrees -= (this.rotationsPerSecond * 360) / 50;
      robotSvg.rotatePart('wheelOne', degrees);
      robotSvg.rotatePart('wheelTwo', degrees);
      robotSvg.rotatePart('wheelThree', degrees);
    }, 20);
    this.robotSvg = robotSvg;
  }

  updated(changedprops) {
    if (changedprops.has("robotArmSetpoint")) {
      this.robotSvg.rotatePart('intakeTopArm', (-60) * (this.robotArmSetpoint / this.robotArmSetpointMax));
    }
    if (changedprops.has("ballCount") || changedprops.has("allianceColor")) {
      if (this.allianceColor == "RED") {
        if (this.ballCount == 1) {
          this.robotSvg.setPartVisibility("redCargo1", true);
          this.robotSvg.setPartVisibility("redCargo2", false);

          this.robotSvg.setPartVisibility("blueCargo1", false);
          this.robotSvg.setPartVisibility("blueCargo2", false);
        } else if (this.ballCount == 2) {
          this.robotSvg.setPartVisibility("redCargo1", true);
          this.robotSvg.setPartVisibility("redCargo2", true);

          this.robotSvg.setPartVisibility("blueCargo1", false);
          this.robotSvg.setPartVisibility("blueCargo2", false);
        } else {
          this.robotSvg.setPartVisibility("redCargo1", false);
          this.robotSvg.setPartVisibility("redCargo2", false);

          this.robotSvg.setPartVisibility("blueCargo1", false);
          this.robotSvg.setPartVisibility("blueCargo2", false);
        }
      } else {
        if (this.ballCount == 1) {
          this.robotSvg.setPartVisibility("blueCargo1", true);
          this.robotSvg.setPartVisibility("blueCargo2", false);

          this.robotSvg.setPartVisibility("redCargo1", false);
          this.robotSvg.setPartVisibility("redCargo2", false);
        } else if (this.ballCount == 2) {
          this.robotSvg.setPartVisibility("blueCargo1", true);
          this.robotSvg.setPartVisibility("blueCargo2", true);

          this.robotSvg.setPartVisibility("redCargo1", false);
          this.robotSvg.setPartVisibility("redCargo2", false);
        } else {
          this.robotSvg.setPartVisibility("blueCargo1", false);
          this.robotSvg.setPartVisibility("blueCargo2", false);

          this.robotSvg.setPartVisibility("redCargo1", false);
          this.robotSvg.setPartVisibility("redCargo2", false);
        }
      }
    }
  }

  render() {
    return html`
      <div class="robot-container"></div>
    `;
  }
}

customElements.define('kwarqs-robot-drawing', RobotSvgElement);

export default {
  elements: {
    'kwarqs-robot-drawing': {
      properties: {
        robotArmSetpoint: { type: 'Number', attribute: 'robot-arm-setpoint' },
        robotArmSetpointMax: { type: 'Number', attribute: 'robot-arm-setpoint-max' },
        rotationsPerSecond: { type: 'Number', attribute: 'rotations-per-second' },
        allianceColor: { type: 'String', attribute: 'allience-color' },
        ballCount: { type: 'Number', attribute: 'ball-count' }
      }
    }
  }
}