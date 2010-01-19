#ifndef ASSIGN04_H
#define ASSIGN04_H

#include "Assign04_global.h"

#include <rws/RobWorkStudioPlugin.hpp>

#include <rw/models/WorkCell.hpp>
#include <rw/kinematics/State.hpp>
#include <rw/kinematics/MovableFrame.hpp>
#include <rwlibs/drawable/RenderFrame.hpp>

#include "ui_Assign04Widget.h"


#include <QString>
#include <QList>
#include <QStringList>

#include <vector>


class ASSIGN04_EXPORT assign04: public RobWorkStudioPlugin, private Ui::Assign04Widget
{
Q_OBJECT
Q_INTERFACES( RobWorkStudioPlugin )

public:
	assign04();
	~assign04();
    virtual void open(rw::models::WorkCell* workcell);

    virtual void close();

    virtual void initialize();

private slots:

	void clickBtnOpen();
  void clickBtnMove();

private:

  rw::kinematics::MovableFrame* _pTestFrame;
  rw::models::WorkCellPtr _pWorkCell;

  const std::vector<rw::kinematics::State> assign04::pathPlanner(
          std::vector<rw::math::Q>& confs,
          const rw::kinematics::State& state);
  
  rw::math::Q assign04::IKSolver(
          const rw::math::Transform3D<double>& baseTtool,
          const rw::kinematics::State& state);

  const std::vector<rw::kinematics::State> assign04::QToStates(
          rw::models::DevicePtr device,
          std::vector<rw::math::Q>& confs,
          const rw::kinematics::State& state);

  int _density;
  std::vector<char> _vLetters;
  std::vector< std::vector<int> > _vX,_vY,_vZ;
  rw::models::DevicePtr _pDevice;
	RobWorkStudio* _pRWS;
  rw::math::Q _home;
  rw::math::Transform3D<double> _origin;
};

#endif // ASSIGN04_H
