package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;

    final private static double LONGITUDE_MOY= Angle.ofDeg(91.929336);
    final private static double LONGITUDE_MOY_PERIGREE= Angle.ofDeg(130.143076);
    final private static double LONGITUDE_NOEUD_ASCENDANT= Angle.ofDeg(291.682547);
    final private static double INCLIN_ORBITE= Angle.ofDeg(5.145396);
    final private static double EXCENT_ORBITE= Angle.ofDeg(0.0549);

    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double longOrbMoyenne= Angle.ofDeg(13.17633966)*daysSinceJ2010+LONGITUDE_MOY;
        double anomalieMoyenne= longOrbMoyenne- Angle.ofDeg(0.1114041)* daysSinceJ2010-LONGITUDE_MOY_PERIGREE;

        double longEclpSoleil= SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).eclipticPos().lon();
        double anomalieMoyenneSoleil=SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion).meanAnomaly();

        double evection=Angle.ofDeg(1.2739)* Math.sin(2*(longOrbMoyenne-longEclpSoleil)-anomalieMoyenne);
        double correcEquAnnu= Angle.ofDeg(0.1858)* Math.sin(anomalieMoyenneSoleil);
        double coorec3= Angle.ofDeg(0.37)* Math.sin(anomalieMoyenneSoleil);

        double anomalieCorr= anomalieMoyenne+evection-correcEquAnnu-coorec3;
        double coorecEquCentre= Angle.ofDeg(6.2886)*Math.sin(anomalieCorr);
        double coorec4= Angle.ofDeg(0.214)*Math.sin(2*anomalieCorr);

        double longOrbitCorr= longOrbMoyenne+evection+coorecEquCentre-correcEquAnnu+coorec4;
        double variation= Angle.ofDeg(0.6583)*Math.sin(2*(longOrbitCorr-longEclpSoleil));

        double longOrbVraie= longOrbMoyenne+variation;

        double longMoyenNoeud= LONGITUDE_NOEUD_ASCENDANT-Angle.ofDeg(0.0529539)*daysSinceJ2010;
        double longCorrNoeud= longMoyenNoeud-Angle.ofDeg(0.16)*Math.sin(anomalieMoyenneSoleil);

        double longLune= Angle.normalizePositive(Math.atan2(Math.sin(longOrbVraie-longCorrNoeud)*Math.cos(INCLIN_ORBITE),
                Math.cos(longOrbVraie-longCorrNoeud))+longCorrNoeud);
        double latLune= Math.asin(Math.sin(longOrbVraie-longCorrNoeud)*Math.sin(INCLIN_ORBITE));
        double phase = (1-Math.cos(longOrbVraie))/2;

        double distance = (1-EXCENT_ORBITE*EXCENT_ORBITE)/(1+EXCENT_ORBITE*Math.cos(anomalieCorr+coorecEquCentre));
        double tailleAngulaire= Angle.ofDeg(0.5181)/distance;

        return new Moon(EquatorialCoordinates.of(longLune,latLune),(float) tailleAngulaire,0f,(float)phase); }
}

