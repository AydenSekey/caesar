/*
Copyright (C) Université du Maine (2013) 

contributeurs : Adrien Duroy, Bastien Andru, Quentin Gosselin, Guillaume Delorme,
 Nicolas Detan, Zubair Parwany, Houda Chouket, Bastien Aubry,
 Vincent Besnard, Ivan Melnychenko

ad.duroy@gmail.com

Ce fichier est une partie de NXTIM.

NXTIM est une bibliothèque logiciel fournissant un modèle objet du code d'un programme
pour un robot NXT. 

NXTIM est régi par la licence CeCILL-C soumise au droit français et
respectant les principes de diffusion des logiciels libres. Vous pouvez
utiliser, modifier et/ou redistribuer ce programme sous les conditions
de la licence CeCILL-C telle que diffusée par le CEA, le CNRS et l'INRIA 
sur le site "http://www.cecill.info".

En contrepartie de l'accessibilité au code source et des droits de copie,
de modification et de redistribution accordés par cette licence, il n'est
offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
seule une responsabilité restreinte pèse sur l'auteur du programme,  le
titulaire des droits patrimoniaux et les concédants successifs.

A cet égard  l'attention de l'utilisateur est attirée sur les risques
associés au chargement,  à l'utilisation,  à la modification et/ou au
développement et à la reproduction du logiciel par l'utilisateur étant 
donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
manipuler et qui le réserve donc à des développeurs et des professionnels
avertis possédant  des  connaissances  informatiques approfondies.  Les
utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
logiciel à leurs besoins dans des conditions permettant d'assurer la
sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
pris connaissance de la licence CeCILL-C, et que vous en avez accepté les
termes.
 */
package nxtim.instruction.validation;

import nxtim.exception.NXTIMBadTypeElementException;
import nxtim.instruction.ExpressionComplexe;
import nxtim.instruction.Operateur;
import nxtim.instruction.TypeElement;

/**
 * Validateur de cohérances des opérandes d'une expression complexe selon l'opérateur.
 */
public class OperandesExpComplexeValidateur implements ExpressionComplexeValidateur {

	/**
	 * Valide les opérandes de l'expression par rapport à l'opérateur.
	 * @throws NXTIMBadTypeElementException si un opérande n'a pas un type cohérent avec l'opérateur.
	 */
	@Override
	public void valider(ExpressionComplexe exp) {
		if(Operateur.isLogiqueBooleenne(exp.getOperateur())) {
			//opérandes doivent être booléens
			if(exp.getMembreDroit() != null && !exp.getMembreDroit().isBooleenne()){
				TypeElement type = exp.getMembreDroit().getType();//Noter le type posant problème
				throw new NXTIMBadTypeElementException(type, "Opérateur de condition (" + exp.getOperateur() + ") invalide avec ce type.");
			}
			else if(exp.getMembreGauche() != null && !exp.getMembreGauche().isBooleenne()) {
				TypeElement type = exp.getMembreGauche().getType();//Noter le type posant problème
				throw new NXTIMBadTypeElementException(type, "Opérateur de condition (" + exp.getOperateur() + ") invalide avec ce type.");
			}
		}
		else {//opérateur logique n'étant pas de logique booléenne et opérateur arithmétique
			Operateur o = exp.getOperateur();
			switch(o) {
				case EGALITE:
					break;
				case AFFECTATION:
					break;
				default:
					// opérateur arithmétique
					// les opérandes ne doivent pas être booléens
					if(exp.getMembreDroit() != null && exp.getMembreDroit().isBooleenne()) {
                        //Noter le type posant problème
						TypeElement type = exp.getMembreDroit().getType();
						throw new NXTIMBadTypeElementException(type, "Opérateur (" + exp.getOperateur() + ") invalide avec ce type.");
					}
					if(exp.getMembreGauche() != null && exp.getMembreGauche().isBooleenne()) {
						//Noter le type posant problème
						TypeElement type = exp.getMembreGauche().getType();
						throw new NXTIMBadTypeElementException(type, "Opérateur (" + exp.getOperateur() + ") invalide avec ce type.");
					}
					break;
			}
		}
	}

}
